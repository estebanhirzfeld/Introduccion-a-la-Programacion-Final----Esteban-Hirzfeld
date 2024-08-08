import java.util.Random;
import java.util.Scanner;

// Entrega Final Esteban Hirzfeld.

// Mi Proyecto Final es un juego simple de BlackJack.
// Hace uso de Scanner y Random para implementar un algoritmo que me pareció muy interesante (Fisher-Yates).

// ACLARACIÓN:
// A lo largo del código se pueden encontrar tanto nombres de funciones como algunos comentarios en inglés.
// Esta es una práctica que acostumbro implementar debido a la naturaleza de la programación, principalmente basada en inglés.

public class MainTerminal {

    // Create Deck
    public static String[][] createDeck() {
        // Utilizo suits para hacer uso de los vectores y a su vez porque tengo planeado
        // utilizarlo con los JOptionPane
        String[] suits = { "H", "D", "C", "S" };
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
        String[][] deck = new String[suits.length * ranks.length][2];

        int index = 0;
        for (String suit : suits) {
            for (String rank : ranks) {
                deck[index][0] = rank;
                deck[index][1] = suit;
                index++;
            }
        }
        return deck;
    }

    // Shuffle Deck - Algoritmo Fisher-Yates :)
    public static void shuffleDeck(String[][] deck) {
        Random rand = new Random();

        for (int i = deck.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            String[] temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

    // Card Letter to Value
    public static int cardToValue(String rank) {
        switch (rank) {
            case "2":
                return Integer.parseInt(rank);
            case "3":
                return Integer.parseInt(rank);
            case "4":
                return Integer.parseInt(rank);
            case "5":
                return Integer.parseInt(rank);
            case "6":
                return Integer.parseInt(rank);
            case "7":
                return Integer.parseInt(rank);
            case "8":
                return Integer.parseInt(rank);
            case "9":
                return Integer.parseInt(rank);
            case "10":
                return Integer.parseInt(rank);
            case "J":
                return 10;
            case "Q":
                return 10;
            case "K":
                return 10;
            case "A":
                return 11;
            default:
                return 0;
        }
    }

    // Cards in hand to Value
    public static int handToValue(String[][] hand) {
        int total = 0;
        int aces = 0;

        for (String[] card : hand) {
            total += cardToValue(card[0]);
            if (card[0].equals("A")) {
                aces++;
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    // Show Hand
    public static void showHand(String[][] hand) {
        for (String[] card : hand) {
            System.out.print(card[0] + card[1] + " ");
        }
        System.out.println();
    }

    // Add Card to Hand
    public static String[][] addCardToHand(String[][] currentHand, String[][] deck, int deckIndex) {
        String[][] newHand = new String[currentHand.length + 1][2];

        for (int i = 0; i < currentHand.length; i++) {
            newHand[i] = currentHand[i];
        }

        newHand[currentHand.length] = deck[deckIndex];

        return newHand;
    }

    // MAIN:
    // Steps:
    // 0 Set chips (2500)
    // 1 create Deck
    // 2 shuffle Deck
    // 3 deliver cards
    // 4 print playerHand and value
    // 5 print dealerHand and value (1 hidden)
    // 6 decide hit or stand (player)
    // 7 hit while value < 17 (dealer)
    // 8 set winnner or tie
    // 9 refresh chips
    // 10 end program or continue

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int playerChips = 2500;
        int playerBet = 0;

        // Game Loop
        while (playerChips > 0) {

            while (true) {
                System.out.println("Set your bet:");
                playerBet = sc.nextInt();
                if (playerBet <= playerChips && playerBet > 0) {
                    break;
                } else {
                    System.out.println("Insufficient chips, please try again:");
                }
            }

            String[][] deck = createDeck();
            shuffleDeck(deck);

            String[][] playerHand = { deck[0], deck[2] };
            String[][] dealerHand = { deck[1], deck[3] };

            int deckIndex = 3;

            showHand(playerHand);
            showHand(dealerHand);

            System.out.println("Player Hand (Value): " + handToValue(playerHand));
            System.out.println("Dealer Hand (Value): " + cardToValue(dealerHand[0][0]) + " 'Hidden' ");

            // Debug - Show Current Dealer Cards
            // System.out.println("\u001B[43m" + "Dealer Cards (Value): " +
            // cardToValue(dealerHand[0][0]) + " "
            // + cardToValue(dealerHand[1][0]));
            // System.out.println("Dealer Hand (Real Value): " + handToValue(dealerHand) +
            // "\u001B[40m");

            // Player's turn, Decide (Hit or Stand) - Terminal
            Boolean playerTurn = true;
            String choice;
            while (playerTurn) {
                while (true) {
                    System.out.println("Do you want to Hit? (y/n)");
                    choice = sc.nextLine().trim().toLowerCase();
                    if (choice.equals("y") || choice.equals("n")) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again:");
                    }
                }
                if (choice.equals("y")) {
                    // Update deck index
                    deckIndex++;

                    playerHand = addCardToHand(playerHand, deck, deckIndex);

                    showHand(playerHand);
                    System.out.println("Player Hand (Value): " + handToValue(playerHand));

                    // Check if player reached 21
                    if (handToValue(playerHand) > 21) {
                        System.out.println(handToValue(playerHand) + ": you lost :(");
                        playerTurn = false;
                    }
                } else {
                    playerTurn = false;
                }
            }

            // Dealer's turn

            // Show Full Hand
            showHand(dealerHand);
            System.out.println("Dealer Hand (Value): " + handToValue(dealerHand));

            // Dealer will hit till value > 17
            while (handToValue(dealerHand) < 17) {
                // Update deck index
                deckIndex++;
                dealerHand = addCardToHand(dealerHand, deck, deckIndex);
                showHand(dealerHand);
                System.out.println("Dealer Hand (Value): " + handToValue(dealerHand));
            }

            // Hand Values Count
            int playerTotal = handToValue(playerHand);
            int dealerTotal = handToValue(dealerHand);

            // Resume
            System.out.println("Final hand value - Player: " + playerTotal + ", Dealer: " + dealerTotal);

            // Set Winner or Tie :)
            if (playerTotal > 21) {
                System.out.println("You Lost! Dealer Wins");
                playerChips = playerChips - playerBet;
            } else if (dealerTotal > 21 || playerTotal > dealerTotal) {
                System.out.println("You Win!");
                playerChips = playerChips + playerBet;
            } else if (playerTotal < dealerTotal) {
                System.out.println("Dealer Wins!");
                playerChips = playerChips - playerBet;
            } else {
                System.out.println("Tie! again?");
                playerChips = playerChips - playerBet;
                playerChips = playerChips + (playerBet / 2);
            }

            // Break if player has no chips
            if (playerChips <= 0) {
                System.out.println("You Lost all your chips :(");
                break;
            }

            // Player can continue if chips > 0
            while (true) {
                System.out.println("Do you want to Play Again? (y/n)");
                choice = sc.nextLine().trim().toLowerCase();
                if (choice.equals("y") || choice.equals("n")) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again:");
                }
            }
            if (!choice.equals("y")) {
                break;
            }
        }
        if (playerChips > 0) {
            System.out.println("Your chips are now: " + playerChips);
        }
        sc.close();
    }

}