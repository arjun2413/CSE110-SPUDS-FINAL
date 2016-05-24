package com.spuds.eventapp.CategoriesList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spuds.eventapp.FilteredCategoryFeed.CategoryFeedTabsFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;


public class CategoriesListFragment extends Fragment {
    public String type;

    public CategoriesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_list, container, false);
        pickCategory(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void pickCategory(View view) {
        academicButton(view);
        sportsButton(view);
        campusButton(view);
        concertsButton(view);
        foodButton(view);
        freeButton(view);
        socialButton(view);
        sportsButton(view);

    }

    public String academicButton(View view) {
        final Button academicButton = (Button) view.findViewById(R.id.academic);
        if (academicButton != null) {
            academicButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_academic);

                    startFeed();
                }
            });
        }
        return type;
    }

    public String sportsButton(View view) {
        final Button sportsButton = (Button) view.findViewById(R.id.sports);
        if (sportsButton != null) {
            sportsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_sports);

                    startFeed();
                }
            });
        }
        return type;
    }

    public String socialButton(View view) {
        final Button socialButton = (Button) view.findViewById(R.id.social);
        if (socialButton != null) {
            socialButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_social);


                    startFeed();
                }
            });
        }
        return type;
    }

    public String freeButton(View view) {
        final Button freeButton = (Button) view.findViewById(R.id.free);
        if (freeButton != null) {
            freeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_free);

                    startFeed();
                }
            });
        }
        return type;
    }

    public String foodButton(View view) {
        final Button foodButton = (Button) view.findViewById(R.id.food);
        if (foodButton != null) {
            foodButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_food);

                    startFeed();
                }
            });
        }
        return type;
    }

    public String concertsButton(View view) {
        final Button concertsButton = (Button) view.findViewById(R.id.concerts);
        if (concertsButton != null) {
            concertsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_concerts);

                    startFeed();
                }
            });
        }
        return type;
    }

    public String campusButton(View view) {
        final Button campusButton = (Button) view.findViewById(R.id.campus_orgs);
        if (campusButton != null) {
            campusButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    type = getString(R.string.cat_student_orgs);

                    startFeed();
                }
            });
        }
        return type;
    }

    void startFeed() {
        Bundle category = new Bundle();
        category.putString(getString(R.string.category_bundle), type);

        CategoryFeedTabsFragment categoryFeedTab = new CategoryFeedTabsFragment();

        categoryFeedTab.setArguments(category);

        ((MainActivity) getActivity()).addSearchToolbar();
        ((MainActivity) getActivity()).searchType = type;
        getFragmentManager().beginTransaction()
                .show(categoryFeedTab)
                .replace(R.id.fragment_frame_layout, categoryFeedTab)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(getString(R.string.fragment_category_feed))
                .commit();
    }

}
