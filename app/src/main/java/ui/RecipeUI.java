package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        displayRecipes(); // レシピ一覧表示を呼び出し
                        break;
                    case "2":
                        addNewRecipe(); // 新規レシピ追加を呼び出し
                        break;
                    case "3":
                        searchRecipe(); // レシピ検索を呼び出し
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        ArrayList<String> recipes = fileHandler.readRecipes();

        if (recipes.isEmpty()) { // レシピデータが空の場合
            System.out.println("No recipes available.");
        } else {
            System.out.println("Recipes:");
            System.out.println("-----------------------------------");
            for (String recipe: recipes) {
                String[] parts = recipe.split(",",2); // レシピ名と材料をカンマで分ける
                System.out.println("Recipe Name: " + parts[0].trim()); // レシピ名を表示
                System.out.println("Main Ingredients: " + (parts.length > 1 ? parts[1].trim() : ""));
                System.out.println("-----------------------------------");
            }
        }
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        System.out.print("Enter recipe name: ");
        String recipeName = reader.readLine().trim();

        System.out.print("Enter main ingredients (comma separated): ");
        String ingredients = reader.readLine().trim();

        fileHandler.addRecipe(recipeName, ingredients);

        System.out.println("Recipe added successfully.");

    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException { // ユーザー入力
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
        String query = reader.readLine().trim();

        String nameCondition = null;
        String ingredientCondition = null;

        String fieldName = ""; // レシピ名の条件の初期化
        String fieldIngredients = ""; // 材料の条件の初期化
        for (int i = 0; i < query.length(); i++) {
            char j = query.charAt(i);
            if (j == '=') {
                fieldName = fieldIngredients;
                fieldIngredients = "";
            } else if (j == '&') {
                if (fieldName.equals("name")) {
                    nameCondition = fieldIngredients;
                } else if (fieldName.equals("ingredient")) {
                    ingredientCondition = fieldIngredients;
                }
            fieldName = "";
            fieldIngredients = "";
        } else {
            fieldIngredients += j;
        }
    }

    if (fieldName.equals("name")) {
        nameCondition = fieldIngredients;
    } else if (fieldName.equals("ingredient")) {
        ingredientCondition = fieldIngredients;
    }

    ArrayList<String> recipes = fileHandler.readRecipes();
    ArrayList<String> matchingRecipes = new ArrayList<>();

    for (String recipe : recipes) {
        String recipeName = ""; // レシピ名
        String ingredients = ""; // 材料
        boolean isCommaFound = false;
        for (int j = 0; j < recipe.length(); j++) {
            char currentChar = recipe.charAt(j);
            if (currentChar == ',' && !isCommaFound) {
                for (int k = 0; k < j; k++) {
                    recipeName += recipe.charAt(k);
                }
                for (int k = j + 1; k < recipe.length(); k++) {
                    ingredients += recipe.charAt(k);
                }
                isCommaFound = true;
                break; // ループを抜ける
            }
        }

        recipeName = recipeName.trim();
        ingredients = ingredients.trim();

        boolean nameMatches = (nameCondition == null || recipeName.contains(nameCondition));
        boolean ingredientMatches = (ingredientCondition == null || ingredients.contains(ingredientCondition));

        if (nameMatches && ingredientMatches) {
            matchingRecipes.add(recipe);
        }
    }
    
    System.out.println("\n");
    System.out.println("Search Results: ");
    if (matchingRecipes.isEmpty()) {
        System.out.println("No recipes found matching the criteria.");
    } else {
        for (String recipe : matchingRecipes) {
            System.out.println(recipe);
        }
    }
    }
}

