package com.projectideagenerator.app;

import java.util.Random;
/**
 * Created by schuyler on 3/8/14.
 * This class is responsible for storing and creating idea phrases
 * Words are hard-coded and stored in lists (makes the most sense on a mobile platform)
 */
public abstract class IdeaGenerator
{
    private static Random ranNum = new Random();
    /*
        Pattern names are listed from left to right, as one would read a phrase
        Key for Syntax:
        D = Description/Descriptive word
        C = [Existing] CSH Project
        B = Buzz-word
        T = Thing
        Pr = Preposition
        Pl = Platform
        L = Language
        * = Kleene Star (to be used or not used at all)
        Logic of a phrase:
        D B* (T || (Pr && C)) (Pr && (Pl || L))
    */
    /*Probability Constants for events*/
    //probability of buzzword appearing
    private static final double buzzProb = 0.30;
    //probability of picking a thing over a CSH project
    private static final double thingOrCSH = 0.70;
    //probability of picking a platform over a language
    private static final double platformOrLanguage = 0.60;
    //Arrays of words to choose from
    private static String[] descptArray = {
        "Musical","Automatic","Robotic","Computational","Mathematical","Functional","Configurable",
        "Spelling","Inquisitive"
    };
    private static String[] CSHArray = {
        "Drink","Media PC","Webnews","Members","ThunderDome","Ride Board","Segfault"
    };
    private static String[] thingArray = {
        "parser","stairs","door","washing machines","school work","radio station","alarm clock",
        "scanner","music player","live stream","video game","fish bowl","simulator","XKCD reader",
        "news feed"
    };
    private static String[] buzzArray = {
        "scalable","cloud-based","social media","inovative","POSIX-compliant","robust",
         "sustainable","synergy","data-mining","Web 2.0","REST-ful","Turing-complete"
    };
    private static String[] prepArray = {
        "but on","on","using","but using","implemented with","implemented on"
    };
    private static String[] platformArray = {
        "Android","iOS","Windows","Mobile Devices","Chrome","Linux","OSX","Puppy Linux","Fedora",
        "Solaris","MySQL","CentOS","MS-DOS","Windows Phone","the web","MongoDB",
        "OpenStack","Tablets","VMs","Arduino","cross-platform","Vim","XML","JSON","JQuery","LaTeX",
        "Kinect","NFC","Machine Learning","Regular Expressions"
    };
    private static String[] languageArray= {
        "C","C#","C++","Python","Assembly","Java","JavaScript","BASIC","Pascal","Ruby","F#","Lisp",
        "Shell-scripts","Objective-C","PEARL","PHP","Haskell"
    };

    //Picks a word at random from a list

    /**
     * Picks a string from the given list at random
     * @param strArray Array of strings to choose from
     * @return A random string from the list with a space after it
     */
    private static String wordFromList(String[] strArray)
    {
        return(strArray[ranNum.nextInt(strArray.length)] + " ");
    }
    /**
     * Generates an idea
     * @return An idea in the form of a string
     */
    public static String generateIdea()
    {
        //Logic of a phrase:
        //D B* (T || (Pr && C)) (Pr && (Pl || L))
        String idea = "";
        idea += wordFromList(descptArray);
        if(ranNum.nextDouble() <= buzzProb)
        {
            idea += wordFromList(buzzArray);
        }
        if(ranNum.nextDouble() <= thingOrCSH)
        {
            idea += wordFromList(thingArray);
        }
        else
        {
            idea += wordFromList(CSHArray);
        }
        idea += wordFromList(prepArray);
        if(ranNum.nextDouble() <= platformOrLanguage)
        {
            idea += wordFromList(platformArray);
        }
        else
        {
            idea += wordFromList(languageArray);
        }
        return(idea);
    }
}