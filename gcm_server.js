/*
    - NodeJS server that communicates with GCM.
    - The Android phone sends messages to GCM with specific actions (such as "invite")
    and GCM sends it to this server. The server then updates the database and sends
    push notifications according to the type of action.
    - The following uses Jonathan's Google Developer API keys
    - The following also has pretty bad code practices
    - Shoutout to David Justo for helping me with asynchronous behavior and promises
*/

var Q = require('q');
var Firebase = require('firebase');

// node-xcs GCM things
var Sender = require('node-xcs').Sender;
var Message = require('node-xcs').Message;
var Notification = require('node-xcs').Notification;
var Result = require('node-xcs').Result;

var SENDER_ID = "";
var API_KEY = "";

var xcs = new Sender(SENDER_ID, API_KEY);
var fbref = new Firebase("https://eventory.firebaseio.com/");

// action values
var user_ids = [];  // array of user ids (Firebase)
var regKeys = [];   // array of user registration keys

// firebase urls
var user_ref = new Firebase("https://eventory.firebaseio.com/users");
var event_ref = new Firebase("https://eventory.firebaseio.com/events");
var comment_ref = new Firebase("https://eventory.firebaseio.com/comments");
var likes_ref = new Firebase("https://eventory.firebaseio.com/likes");
var reg_ref = new Firebase("https://eventory.firebaseio.com/events_registrations");

// gcm notification things
var notification;
var xcs_message;

console.log("Started");


// When GCM receives a message, receive from here
xcs.on('message', function(messageId, from, data, category) {
    /* ----------------------------------------------------------------------------
        Invite Logic:
            For each item in data.data (String(Array of Firebase user_ids)), get the
            registration key and push into an array. Query the event name from data.event_id.
            Create a notification message "[FROM] has invited you to [EVENT_NAME]"
            Send values to createInviteNotification(), which adds to Firebase and sends push notif
            Uses user_ref and event_ref

        Received Values:
            data.action     -   action of the event. In this case "invite"
            data.event_id   -   firebase id of the event
            data.data       -   string of an array of firebase user ids
            data.host       -   the person inviting others to an event
            data.host_id    -   the firebase user id of the inviter
    ----------------------------------------------------------------------------- */
    if (data.action == "invite") {
        user_ids = stringToArr(data.data); // convert the string of users to an array of users
        regKeys = [];
        event_name = "";
        promises = [];

        /*
            For every user_id in the table, get the registration key
            Loop through elements and push user key into getRegistrationKey function
            getRegistrationKey functions are pushed into an array of promises
            Will return the snapshot values
        */
        user_ids.forEach(function(key){
            promises.push(getRegistrationKey(key));
        });

        // once the keyes have been received, log the data
        Q.allSettled(promises)
            // data is the snapshot.val(), but we manipulate the regKeys
            // within this block, we should have the regKeys, a snapshot
            // get the event name
            .then(function() {
                // get the event name based on the firebase event_id
                var def = Q.defer();
                event_ref.orderByKey().equalTo(data.event_id).on('child_added', function(snapshot) {
                    // return the event name
                    def.resolve(snapshot.val().event_name);
                });
                // return the event name promise
                return def.promise;
            })
            // create the invite notification
            .then(function(event_name){
                console.log("Event name received: " + event_name);
                var defer = Q.defer();
                var push_message = data.host + " has invited you to " + event_name;
                var title_message = "Eventory - You have been invited to an event!"

                createNotification(regKeys, push_message, title_message, "invite", event_name, data.host, data.host_id, data.event_id, function(){
                    defer.resolve(true);
                });
                return defer.promise;
            })
            .then(function() {
                console.log("Completed sending all Invite notifications");
                console.log("----------------");
            });
    }

    /* ----------------------------------------------------------------------------
        Reply Logic:
            Query all the follower registration keys based on the comment_id (likes table)
            and push those values into an array. Pass to createReplyNotifications table
            Uses user_ref and event_ref. Gets replier_id, comment_id, and event_id
            Loops similarly to the invite logic

        Received Values:
            data.action     -   action of the event. In this case "reply"
            data.item_id    -   firebase id of the comment
            data.host_id    -   firebase id of the host of the event
            data.event_id   -   firebase id of the event
            data.host  -   the replier's name
    ----------------------------------------------------------------------------- */
    else if (data.action == "reply") {
        user_ids = []
        regKeys = [];
        event_name = "";
        promises = [];

        // get the number of likes for a comment
        (function() {
            promises.push(function() {
                return true;
            });
        })();

        Q.allSettled(promises)
            // get all the user ids
            .then(function() {
                var promises = [];
                var def = Q.defer();

                likes_ref.orderByKey().on('child_added', function(snapshot) {
                    if (data.item_id == snapshot.val().comment_id) {
                        user_ids.push(snapshot.val().user_id);
                    }
                    def.resolve(user_ids);
                });
                return def.promise;
            })
            // get all registration keys
            .then(function() {
                var defer = Q.defer();
                var promises = [];

                user_ids.forEach(function(key) {
                    promises.push(getRegistrationKey(key));
                });
                defer.resolve(true);
                return defer.promise;
            })
            // get the event name based on firebase event_id
            .then(function() {
                var def = Q.defer();

                event_ref.orderByKey().equalTo(data.event_id).on('child_added', function(snapshot) {
                    def.resolve(snapshot.val().event_name);
                });
                return def.promise;
            })
            // create the reply notification
            .then(function(event_name) {
                var defer = Q.defer();
                var push_message = data.host + " has replied to a comment you liked!";
                var title_message = "Eventory - Someone replied to your comment!"

                createNotification(regKeys, push_message, title_message, "reply", event_name, data.host, data.host_id, data.event_id, function() {
                    defer.resolve(true);
                })
                return defer.promise;
            })
            .then(function() {
                console.log("Finished sending out reply notifications");
                console.log("---------------");
            });
    }


    /* ----------------------------------------------------------------------------
        Update Logic:
            Query all the registration keys of users going to an event.
            For all of the registration keys, send a push notification.
            Create a notification "[EVENT_NAME] has been updated!"

        Received Values:
            data.action     -   action of the event. In this case "reply"
            data.item_id    -   firebase id of the event
            data.host_id    -   firebase id of the host of the event
            data.event_id   -   firebase id of the event
            data.host_name  -   the event's host's name
    ----------------------------------------------------------------------------- */
    else if (data.action == "update") {
        user_ids = []; // convert the string to an array
        regKeys = [];
        event_name = "";
        promises = [];
        user_ids = [];

        console.log("Update");

        // just to start off, bad practice, not sure how to get around this
        (function() {
            promises.push(function() {
                return true;
            });
        })();

        Q.allSettled(promises)
            .then(function(data) {
                console.log("Received likes");
            })
            // get the user ids of an event
            .then(function() {
                console.log("Getting ids");
                var def = Q.defer();

                reg_ref.orderByKey().on('child_added', function(snapshot) {
                    if (data.event_id == snapshot.val().event_id) {
                        user_ids.push(snapshot.val().user_id);
                    }
                    def.resolve(user_ids);
                });
                return def.promise;
            })
            // get all registration keys
            .then(function() {
                var def = Q.defer();
                var promises = [];

                user_ids.forEach(function(key) {
                    promises.push(getRegistrationKey(key));
                });
                def.resolve(true);
                return def.promise;
            })
            // get the event name based on the firebase event_id
            .then(function() {
                console.log("Getting name");
                var def = Q.defer();

                event_ref.orderByKey().equalTo(data.event_id).on('child_added', function(snapshot) {
                    def.resolve(snapshot.val().event_name);
                });
                return def.promise;
            })
            // create the reply notification
            .then(function(event_name) {
                var defer = Q.defer();
                var push_message = event_name + " has been updated!"
                var title_message = "Eventory - An event you're following has been updated!"

                createNotification(regKeys, push_message, title_message, "update", event_name, data.host, data.host_id, data.event_id, function() {
                    defer.resolve(true);
                })
                return defer.promise;
            })
            .then(function() {
                console.log("Finished sending out update notification");
                console.log("---------------");
            });
    }

    // do nothing if we somehow send the wrong action
    else {
        console.log("invalid action");
    }
});

/* -----------------------------------------------------------------------------
    getRegistrationKey: Returns a promise for Q.allSettled. Async function for synch promise
    Params:
        key - takes a key of the Firebase user id
----------------------------------------------------------------------------- */
function getRegistrationKey(key){
    console.log("Getting registration keys");
    var def = Q.defer();

    user_ref.orderByKey().equalTo(key).on("child_added", function(snapshot) {
        regKeys.push(snapshot.val().registration_id);
        // resolve sets the promise value
        def.resolve(snapshot.val());
    });
    // returns what has been resolved - snapshot.val()
    return def.promise;
}


/* -----------------------------------------------------------------------------
    createNotification: calls addNotification() and pushNotification()
    Params:
        recipients: array of registration keys (Strings) for recipients (for push)
        push_message: the message of the push notification
        title_message: the title of the push notificaiton
        type: the type of notification (eg. "invite")
        event_name: String of the event_name users are being invited to
        host_name: String of the user inviting other users
        host_id: String of the Firebase id for the host
        event_id: String of the Firebase id for the event
----------------------------------------------------------------------------- */
function createNotification(recipients, push_message, title_message, type, event_name, host_name, host_id, event_id, callback) {
    console.log("Creating " + type +  " notification");

    var defer = Q.defer();
    var promises = []

    // add a notification for all of the recipients in Firebase
    if (type == "reply" || type == "update") {
        recipients.forEach(function(val,i){
            promises.push(addNotification(event_id, push_message, type, val, user_ids[i]));
        });
    }
    else {
        recipients.forEach(function(val){
            promises.push(addNotification(event_id, push_message, type, val, host_id));
        });
    }

    Q.allSettled(promises)
        // create a push notification for the recipients
        .then(function(values){
            console.log("Creating push");
            var defer = Q.defer();
            // send the push notification for all of the recipients
            pushNotification(recipients, push_message, title_message, function() {
                defer.resolve(true);
            });
            return defer.promise;
        })
        .then(function(){
            defer.resolve(true);
        })
        .then(callback);
    return defer.promise;
}

/* -----------------------------------------------------------------------------
    pushInviteNotification: sends the push notification per recipient
    Params:
        recipients: array of registration keys (Strings) for recipients of invite
        push_message: String message of what we will push to each user
        host_name: String of the user inviting other users
        event_name: String of the event's name
----------------------------------------------------------------------------- */
function pushNotification(recipients, push_message, title_message, callback) {
    console.log("Pushing notification");

    var defer = Q.defer();
    var promises = [];

    // send push notification for every recipient
    recipients.forEach(function(val) {
        promises.push((val, title_message, push_message));
    });

    // once the push notifications have been sent synchronously, fire callback
    Q.allSettled(promises).then(callback);
}

/* -----------------------------------------------------------------------------
    sendMessage: used for synchronous promise, sends a push notification to recipient
    Params:
        recipient: the Android Registration ID of the user receiving the notification
----------------------------------------------------------------------------- */
function sendMessage(recipient, title_message, push_message) {
    var defer = Q.defer();
    var notification = new Notification("ic_launcher")
        .title(title_message)
        .body(push_message)
        .build();
    var xcs_message = new Message("MessageID_" + makeid())
        .priority("high")
        .dryRun(false)
        .deliveryReceiptRequested(true)
        .notification(notification)
        .build();

    xcs.sendNoRetry(xcs_message, recipient, function(result) {
        if (result.getError()) {
            console.error(result.getErrorDescription());
        }
        else {
            console.log("Message has been sent to " + recipient);
        }
        defer.resolve(true);
    });
    return defer.promise;
}

/* -----------------------------------------------------------------------------
    makeId: creates a random 10 digit id for the message, from stack overflow
----------------------------------------------------------------------------- */
function makeid() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 10; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    return text;
}

/* -----------------------------------------------------------------------------
    addNotification: Adds notification data to the Firebase database
    Params:
        id_event: String of the event's Firebase id
        message_string: String of the message we're pushing
        id_type: String of the type of notification (invite, reply, update)
        id_notified: String of the Firebase id of the user being notified
        id_user: String of the Firebase id of the user notifying others (NULL if Update Event)
----------------------------------------------------------------------------- */
function addNotification(id_event, message_string, id_type, id_notified, id_user) {
    console.log("Creating notification");
    var notifRef = fbref.child("notifications");
    var newNotifRef = notifRef.push();
    var defer = Q.defer();

    newNotifRef.set({
        date: getToday(),
        event_id: id_event,
        message: message_string,
        notification_type: id_type,
        notified_id: id_notified,
        user_id: id_user
    },function(){
        defer.resolve(true);
    });
    return defer.promise;
}

/* -----------------------------------------------------------------------------
    getToday: gets today's time in MM/DD/YYYY format. From stackoverflow
----------------------------------------------------------------------------- */
function getToday() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!
    var yyyy = today.getFullYear();

    if(dd<10) {
        dd='0'+dd
    }

    if(mm<10) {
        mm='0'+mm
    }

    today = mm+'/'+dd+'/'+yyyy;
    return today
}

/* -----------------------------------------------------------------------------
    stringToArray: converts a string that is an array to an array
    Params:
        arr: the string that is an array. Eg. '['item', 'element']'
----------------------------------------------------------------------------- */
function stringToArr(arr){
    var return_array = [];

    arr = arr.substring(1,arr.length -1);
    arr = arr.split(",");

    for (i = 0; i < arr.length; i++ ) {
        arr[i] = arr[i].trim();
        return_array.push(arr[i]);
    }
    return return_array;
}
