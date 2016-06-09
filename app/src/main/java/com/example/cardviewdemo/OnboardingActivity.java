package com.example.cardviewdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

/**
 * Created by anuragsinha on 16-06-06.
 */
public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_main_layout);

        PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView), getDataForOnboarding(), getApplicationContext());

        engine.setOnChangeListener(new PaperOnboardingOnChangeListener() {
            @Override
            public void onPageChanged(int oldElementIndex, int newElementIndex) {
                //Toast.makeText(getApplicationContext(), "Swiped from " + oldElementIndex + " to " + newElementIndex, Toast.LENGTH_SHORT).show();
            }
        });

        engine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Toast.makeText(getApplicationContext(), "Welcome to CardB !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                // Probably here will be your exit action
            }
        });

    }

    // Just example data for Onboarding
    private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
        // prepare data
        PaperOnboardingPage scr0 = new PaperOnboardingPage("Welcome to CardB !", "A CMS Quality Control Tool",
                Color.parseColor("#666666"), R.drawable.page1, R.drawable.swipe);
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Visual Bliss", "Movies, TV shows, Sports, we've got it all! Does it meet your design reqirements ?",
                Color.parseColor("#444444"), R.drawable.page2, R.drawable.swipe);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Die hard fan.", "Celebrity facts, Bio and brains behind the scenes. Why is More = Little?",
                Color.parseColor("#222222"), R.drawable.page3, R.drawable.swipe);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Got Tidbits, Have Story!", "Search by all possible metadata fields",
                Color.parseColor("#000000"), R.drawable.page4, R.drawable.swipe);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr0);
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;
    }
}

