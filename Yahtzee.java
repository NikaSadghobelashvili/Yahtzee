/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() 
	{
		int currentPlayer = 0;
		scorecard = new int[nPlayers][17];
		existingCategories = new int[nPlayers][15];
		int[][] diceValues = new int[nPlayers][N_DICE];
	    
	    for (int round = 0; round < N_SCORING_CATEGORIES*nPlayers; round++) 
	    {
	        display.printMessage(playerNames[currentPlayer] + "'s turn! Click 'Roll Dice' button.");
	        display.waitForPlayerToClickRoll(currentPlayer + 1);
	        
	        rollDice(diceValues,currentPlayer); //initial roll
	        
	        reroll(diceValues,currentPlayer); //first re-roll
	        
	        reroll(diceValues,currentPlayer); //second re-roll
	        
	       // Category selection
	       int selectedCategory=selectCategory(currentPlayer);
	       int score = categoryValidation(currentPlayer,diceValues,selectedCategory);
	       display.updateScorecard(selectedCategory, currentPlayer+1, score);
	       
	       scorecard[currentPlayer][selectedCategory-1]=score; //Inputing information about scores in Score-card array
	      
	       currentPlayer = (currentPlayer + 1) % nPlayers;   // cycle through players, when current player reaches value of nPlayer it becomes 0 again
	    }
	    updateFinalScores(nPlayers,scorecard);
	}
	
	private int sumOfNumbers(int[] arr, int number)
	{
		int sum=0;
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]==number)
			{
				sum+=number;
			}
		}
		return sum;
	}
	
	private int sumOfDices(int[] arr)
	{
		int sum=0;
		for(int i=0;i<arr.length;i++)
		{
			sum+=arr[i];
		}
		return sum;
	}
	
	private int categoryScore(int selectedCategory,int[] arr)
	{
		switch (selectedCategory) {
        case CHANCE:
            return sumOfDices(arr);
        case YAHTZEE:
            return 50;
        case LARGE_STRAIGHT:
            return 40;
        case SMALL_STRAIGHT:
            return 30;
        case FULL_HOUSE:
            return 25;
        case FOUR_OF_A_KIND:
            return sumOfDices(arr);
        case THREE_OF_A_KIND:
            return sumOfDices(arr);
        case SIXES:
            return sumOfNumbers(arr,selectedCategory);
        case FIVES:
            return sumOfNumbers(arr,selectedCategory);
        case FOURS:
            return sumOfNumbers(arr,selectedCategory);
        case THREES:
            return sumOfNumbers(arr,selectedCategory);
        case TWOS:
            return sumOfNumbers(arr,selectedCategory);
        case ONES:
            return sumOfNumbers(arr,selectedCategory);
        default:
            return 0; 
    }
	}
	
	private int[] sort(int[] diceValues) //simple bubble sort algorithm
	{
		for (int i = 0; i < diceValues.length - 1; i++) {
            for (int j = 0; j < diceValues.length - i - 1; j++) {
                if (diceValues[j] > diceValues[j + 1]) {
                    int temp = diceValues[j];
                    diceValues[j] = diceValues[j + 1];
                    diceValues[j + 1] = temp;
                }
            }
		}
		return diceValues;
	}
	
	private boolean checkIfStraight(int[] diceValues, int length)
	{
		int count = 1; 
		
	    for (int i = 1; i < diceValues.length; i++) {
	        if (diceValues[i] == diceValues[i - 1] + 1) {
	            count++;
	            if (count >= length) {
	                return true; 
	            }
	        } else if (diceValues[i] == diceValues[i - 1]) {
	            
	            continue;
	        }
	        else
	        {
	        	count=1;
	        }
	    }
	    return false; 
	}
	
	private boolean checkIfFullHouse(int[] arr)
	{
		if(arr[0]==arr[1] && arr[2]==arr[3] && arr[2]==arr[4])
		{
			return true;
		}
		else if(arr[0]==arr[1]&&arr[0]==arr[2]&&arr[3]==arr[4])
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean checkIfTFK(int[] arr, int length)
	{
			int count = 1; 
		    for (int i = 1; i < arr.length; i++) 
		    {
		        if (arr[i] == arr[i - 1]) 
		        {
		            count++;
		            if (count >= length) 
		            {
		                return true; 
		            }
		        } 
		        else if (arr[i] != arr[i - 1]) 
		        {
		            
		            count = 1;
		        }
		    }
		    return false;
	}
	
	
	private boolean checkCategory(int[] diceValues, int selectedCategory)
	{
		if(selectedCategory==CHANCE)
		{
			return true;
		}
		else if(selectedCategory==YAHTZEE)
		{
			int temp=diceValues[0];
			for(int i =1;i<diceValues.length;i++)
			{
				if(diceValues[i]!=temp) return false;
			}
			return true;
		}
		else if(selectedCategory==LARGE_STRAIGHT)
		{
			int[] sortedArr = sort(diceValues);
			return checkIfStraight(sortedArr,5);
		}
		else if(selectedCategory==SMALL_STRAIGHT)
		{
			int[] sortedArr = sort(diceValues);
			return checkIfStraight(sortedArr,4);
		}
		else if(selectedCategory==FULL_HOUSE)
		{
			int[] sortedArr = sort(diceValues);
			return checkIfFullHouse(sortedArr);
		}
		else if(selectedCategory==THREE_OF_A_KIND)
		{
			int[] sortedArr = sort(diceValues);
			return checkIfTFK(sortedArr,3);
		}
		else if(selectedCategory==FOUR_OF_A_KIND)
		{
			int[] sortedArr = sort(diceValues);
			return checkIfTFK(sortedArr,4);
		}
		else if(selectedCategory>=1&&selectedCategory<=6)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private int selectCategory(int currentPlayer)
	{
		display.printMessage("Select category.");
        int selectedCategory = display.waitForPlayerToSelectCategory();
        while(contains(existingCategories[currentPlayer],selectedCategory))
        {
        	display.printMessage("You have already chosen this category, please choose another");
        	selectedCategory = display.waitForPlayerToSelectCategory();
        }
        addToCategories(currentPlayer,selectedCategory);
        return selectedCategory;
	}
	
	private int categoryValidation(int currentPlayer, int[][] diceValues, int selectedCategory)
	{
		 boolean isValidCategory = checkCategory(diceValues[currentPlayer], selectedCategory);
	     int score = isValidCategory ? categoryScore(selectedCategory,diceValues[currentPlayer])  : 0;
	     return score;
	}
	
	private void updateFinalScores(int nPlayer, int[][] scorecard)
	{
		for(int i=0; i<nPlayer;i++)
		{
			updateFinalScoreForCurrentPlayer(scorecard,i);
		}
	}
	
	private void updateFinalScoreForCurrentPlayer(int[][] scorecard, int currentPlayer)
	{
		int upperScore = calculateUpperScore(scorecard[currentPlayer]);
	    int upperBonus = (upperScore >= 63) ? 35 : 0;
	    int lowerScore = calculateLowerScore(scorecard[currentPlayer]);
	    int totalScore = upperScore + upperBonus + lowerScore;

	    display.updateScorecard(UPPER_SCORE, currentPlayer+1, upperScore);
	    display.updateScorecard(UPPER_BONUS, currentPlayer+1, upperBonus);
	    display.updateScorecard(LOWER_SCORE, currentPlayer+1, lowerScore);
	    display.updateScorecard(TOTAL, currentPlayer+1, totalScore);
	}
	
	private int calculateUpperScore(int[] upperScores) 
	{
	    int sum = 0;
	    for (int i = 0; i < 6; i++) 
	    {
	        sum += upperScores[i];
	    }
	    return sum;
	}

	private int calculateLowerScore(int[] scorecard) 

	{
	    int sum = 0;
	    for (int i = 8; i <15; i++) 
	    { 
	        sum += scorecard[i];
	    }
	    return sum;
	}
	
	private boolean contains(int[] categories, int category)
	{
		for(int i =0; i<categories.length;i++)
		{
			if(i==6||i==7)
			{
				continue;
			}
			if(categories[i]==category)
			{
				return true;
			}
		}
		return false;
	}
	
	private void reroll(int[][] diceValues, int currentPlayer)
	{
		display.printMessage("Select the dice you want to reroll again and click 'Roll Again'.");
	       
        display.waitForPlayerToSelectDice();
        
        rollDiceForReroll(diceValues,currentPlayer);
	}
	
	private void rollDice(int[][] diceValues, int currentPlayer)
	{
		for (int die = 0; die < N_DICE; die++) 
        {
            diceValues[currentPlayer][die] = rgen.nextInt(1, 6);
        }
		display.displayDice(diceValues[currentPlayer]);
	}
	
	private void rollDiceForReroll(int[][] diceValues, int currentPlayer)
	{
		for (int die = 0; die < N_DICE; die++) 
        {
			if(display.isDieSelected(die))
			{
			diceValues[currentPlayer][die] = rgen.nextInt(1, 6);
			}
        }
		display.displayDice(diceValues[currentPlayer]);
	}
	
	private void addToCategories(int currentPlayer, int category)
	{
		existingCategories[currentPlayer][category-1]=category;
	}
		
/* Private instance variables */
	private int[][] scorecard;
	private int[][] existingCategories;
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
