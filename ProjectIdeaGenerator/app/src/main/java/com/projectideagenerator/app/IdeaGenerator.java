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
    private static final double buzzProb = 0.50;
    //probability of picking a thing over a CSH project
    //we want original ideas here, people!
    private static final double thingOrCSH = 0.70;
    //probability of picking a platform over a language
    private static final double platformOrLanguage = 0.60;
    //Arrays of words to choose from
    private static String[] descptArray = {
        "Musical","Automatic","Robotic","Computational","Mathematical","Functional","Configurable",
        "Spelling","Inquisitive","Reliable","Electric","Electronic-based", "Enterprise", "Super-duper",
        "Mnemonic", "Integrated","Superior", "Thought-provoking", "Virtual", "Tactical", "Strategic",
        "Technical", "Future-proofed", "Ethical", "Climatic", "Unacceptable", "Complicated",
        "Over-engineered", "Tactic-cool", "Flexible", "Remote-controlled", "Wifi-enabled",
        "Cutting-edge", "Digital", "Viral", "Bacon-flavored", "Trendy", "Ergonomic", "Extreme",
        "Viable"
    };
    private static String[] CSHArray = {
        "Drink","Media PC","Webnews","Members Database","ThunderDome","Ride Board","Segfault","Auto-Drink Admin",
        "WaLLBoT", "Iron-Chef", "Arcade cabinets", "Gatekeeper", "Food Portal"
    };
    private static String[] thingArray = {
        "string parser","stairs","door","washing machine","school work scheduler","radio station","alarm clock",
        "flat-bed scanner","music player","live stream","video game","fish bowl","cat simulator","XKCD reader",
        "news feed","name generator","3D printer","data analyzer","fish feeder","candy machine","TV tuner","fan",
        "wheel chair", "chair", "table-top RPG", "mp3 player", "code interpreter", "code compiler",
        "network analyzer", "wifi hotspot", "speakers", "LED array", "LED controller", "LED cube", "flux capacitor",
        "encryption scheme", "code library", "code plugin", "logging simulator", "catapult", "cat launcher", "rhythm game",
        "FPS game", "indie game", "Nerf gun", "trash compactor", "personal drone", "personal assistant", "typewriter",
        "pizza maker", "food cooker", "personal safe", "e-reader", "potato cannon", "ASL interpreter", "radar detector",
        "bug zapper", "fish finder", "flying toaster", "model airplane", "video format", "artwork", "butt scratcher",
        "synthesizer", "hammer", "Twitch Plays Pokemon", "clock", "point & click adventure", "card game",
        "map-maker", "image manager", "vacuum cleaner", "window manager", "ssh client", "IM client", "IRC bot",
        "IRC client", "light switch", "puzzle game", "physics engine", "calculator", "statistics service",
        "file server", "lighting engine", "click-counter", "string manipulator", "puzzle solver", "news scraper",
        "web scraper"
    };
    private static String[] buzzArray = {
        "scalable","cloud-based","social media","inovative","POSIX-compliant","robust",
         "sustainable","synergy","data-mining","Web 2.0","REST-ful","Turing-complete","server-side",
         "dynamically-allocated", "distributed computing", "BitCoin", "crypto-currency", "stream-lined",
         "team-driven", "(such wow)", "Beekman-approved", "plug and play", "solar-powered", "hybrid",
         "cost-containment", "voice recognition"
    };
    private static String[] prepArray = {
        "on","using","implemented with","implemented using"
    };
    private static String[] CSHprepArray = {
        "but on", "but using","but implemented with"
    };
    private static String[] platformArray = {
        "Android","iOS","Windows","Mobile Devices","Chrome","Linux","OSX","Puppy Linux","Fedora",
        "Solaris","MySQL","CentOS","MS-DOS","Windows Phone","the web","MongoDB",
        "OpenStack","Tablets","VMs","Arduino","cross-platform","Vim","XML","JSON","JQuery","LaTeX",
        "Kinect","NFC","Machine Learning","Regular Expressions", "Steam Box", "Maven", "IntelliJ",
        "Google Glass", "Raspberry Pi", "genetic algorithms"
    };
    private static String[] languageArray= {
        "C","C#","C++","Python","Assembly","Java","JavaScript","BASIC","Pascal","Ruby","F#","Lisp",
        "Shell-scripts", "Objective-C", "PEARL", "PHP", "Haskell", "Hadoop", "XNA", "Google DART"
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
        //D B* (T || C)) (Pr && (Pl || L))
        String idea = "";
        idea += wordFromList(descptArray);
        if(ranNum.nextDouble() <= buzzProb)
        {
            idea += wordFromList(buzzArray);
        }
        if(ranNum.nextDouble() <= thingOrCSH)
        {
            idea += wordFromList(thingArray);
            idea += wordFromList(prepArray);
        }
        else
        {
            idea += wordFromList(CSHArray);
            idea += wordFromList(CSHprepArray);
        }
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

    /**
     * Counts current number of ideas possible
     * @return The number of ideas possible
     */
    public static int calculateIdeas()
    {
        //Logic of a phrase:
        //D B* (T || C)) (Pr && (Pl || L))
        //over used
        int descSize = descptArray.length;
        int buzzSize = buzzArray.length;
        int platSize = platformArray.length;
        int langSize = languageArray.length;
        //smaller calculations
        int thingAndPrep = thingArray.length * prepArray.length;
        int CSHAndPrep = CSHArray.length * CSHprepArray.length;
        //bigger calcs (of independent scenarios types)
        //things
        int DTPl = descSize * thingAndPrep * platSize;
        int DTL = descSize * thingAndPrep * langSize;
        int DBTPl = descSize * buzzSize * thingAndPrep * platSize;
        int DBTL = descSize * buzzSize * thingAndPrep * langSize;
        //CSH things
        int DCPl = descSize * CSHAndPrep * platSize;
        int DCL = descSize * CSHAndPrep * langSize;
        int DCTPl = descSize * buzzSize * CSHAndPrep * platSize;
        int DCTL = descSize * buzzSize * CSHAndPrep * langSize;
        int num = DTPl + DTL + DBTPl + DBTL + DCPl + DCL + DCTPl + DCTL;
        return(num);
    }
}