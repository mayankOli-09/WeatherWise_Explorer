package com.example;

import java.util.Scanner;

public class UIModule 
{

    private final Scanner scanner = new Scanner(System.in);
    private final WeatherModule weatherModule = new WeatherModule();
    private final CityInfoModule cityModule = new CityInfoModule();
    private final RecommendationModule recModule = new RecommendationModule();
    private final SearchModule searchModule = new SearchModule();
    private final DataManager dataManager = new DataManager();

    public void run() 
    {
        System.out.println("\n Initializing WeatherWise Explorer...");
        System.out.println(" Welcome to WeatherWise — Your Smart City Guide!\n");

        boolean running = true;
        while (running) 
        {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) 
            {
                case "1" -> 
                {
                    String city = askCity();
                    weatherModule.displayWeather(city);
                    dataManager.saveSearchHistory("Checked Weather: " + city);
                }
                case "2" -> 
                {
                    String city = askCity();
                    cityModule.displayCityInfo(city);
                    dataManager.saveSearchHistory("Viewed City Info: " + city);
                }
                case "3" ->
                {
                    String city = askCity();
                    recModule.recommend(city);
                    dataManager.saveSearchHistory("Got Recommendations: " + city);
                }
                case "4" -> 
                {
                    System.out.print("\n  Enter keyword (e.g. temple, nature, park): ");
                    String keyword = scanner.nextLine();
                    searchModule.search(keyword);
                    dataManager.saveSearchHistory("Searched: " + keyword);
                }
                case "5" -> dataManager.loadSearchHistory();
                case "6" -> 
                {
                    System.out.println("\n  Thank you for using WeatherWise. Safe travels and we hope to see you soon!");
                    running = false;
                }
                default -> System.out.println("\n  Invalid choice. Please pick 1-6.");
            }
        }
    }

    private void showMenu() 
    {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("        WEATHERWISE EXPLORER");
        System.out.println("=".repeat(45));
        System.out.println("  1.  View Live Weather");
        System.out.println("  2.  Explore City Information");
        System.out.println("  3.  Get Smart Recommendations");
        System.out.println("  4.  Search Places");
        System.out.println("  5.  View Search History");
        System.out.println("  6.  Exit");
        System.out.println("=".repeat(45));
        System.out.print("  Select (1-6): ");
    }

    private String askCity() 
    {
        System.out.print("\n  Enter city (dehradun / mussoorie / rishikesh / haridwar): ");
        return scanner.nextLine().trim();
    }

    public static void main(String[] args) 
    {
        new UIModule().run();
    }
}