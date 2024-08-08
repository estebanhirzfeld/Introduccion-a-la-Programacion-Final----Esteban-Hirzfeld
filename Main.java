import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Objects;

// Entrega Final Esteban Hirzfeld.

// Mi Proyecto Final es un juego simple de BlackJack.
// Hace uso de Scanner y Random para implementar un algoritmo que me pareció muy interesante (Fisher-Yates).

// ACLARACIÓN:
// A lo largo del código se pueden encontrar tanto nombres de funciones como algunos comentarios en inglés.
// Esta es una práctica que acostumbro implementar debido a la naturaleza de la programación, principalmente basada en inglés.

public class Main {

    // UI

    public static int optionInput(String message, String title, String path) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(Main.class.getResource(path)));

        // Create a custom panel with the image
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(new JLabel(image), BorderLayout.CENTER);

        // Define custom button texts
        Object[] options = { "Yes", "No" };

        // Show the confirm dialog
        int result = JOptionPane.showOptionDialog(null, panel, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        return result;
    }

    public static String messageInput(String message, String title, String path) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(Main.class.getResource(path)));

        // Set custom font size for the JOptionPane
        UIManager.put("OptionPane.messageFont", new Font("Roboto", Font.PLAIN, 20));
        UIManager.put("OptionPane.buttonFont", new Font("Roboto", Font.PLAIN, 20));
        UIManager.put("TextField.font", new Font("Roboto", Font.PLAIN, 20));

        String result = (String) JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, image,
                null, null);

        // Reset the UIManager properties to their defaults if needed
        UIManager.put("OptionPane.messageFont", UIManager.getDefaults().getFont("OptionPane.messageFont"));
        UIManager.put("OptionPane.buttonFont", UIManager.getDefaults().getFont("OptionPane.buttonFont"));
        UIManager.put("TextField.font", UIManager.getDefaults().getFont("TextField.font"));

        return result;
    }

    public static void showMessage(String message, String title, String path) {
        ImageIcon image = new ImageIcon(Objects.requireNonNull(Main.class.getResource(path)));

        // Set custom font size for the JOptionPane
        UIManager.put("OptionPane.messageFont", new Font("Roboto", Font.PLAIN, 20));
        UIManager.put("OptionPane.buttonFont", new Font("Roboto", Font.PLAIN, 20));

        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, image);

        // Reset the UIManager properties to their defaults if needed
        UIManager.put("OptionPane.messageFont", UIManager.getDefaults().getFont("OptionPane.messageFont"));
        UIManager.put("OptionPane.buttonFont", UIManager.getDefaults().getFont("OptionPane.buttonFont"));
    }

    public static void showHandPane(String title, String[][] hand, Boolean hidden) {
        // Panel
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));

        // Define the target size for the images
        int targetWidth = 230;
        int targetHeight = 350;

        for (int i = 0; i < hand.length; i++) {
            String imagePath;

            if (hidden && i == hand.length - 1) {
                imagePath = "/ui/cards/red_back.png";
            } else {
                imagePath = "/ui/cards/" + hand[i][0] + hand[i][1] + ".png";
            }
            // Load the image
            ImageIcon originalIcon = new ImageIcon(
                    Objects.requireNonNull(Main.class.getResource(imagePath)));
            Image originalImage = originalIcon.getImage();

            // Resize the image
            Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            // Add the resized image to the panel
            imagePanel.add(new JLabel(resizedIcon));

            // Add Margin
            imagePanel.add(Box.createHorizontalStrut(10));
        }

        // Message
        String message = "Dealer Hand: ";

        for (int i = 0; i < hand.length; i++) {
            if (hidden && i == hand.length - 1) {
                message = message + "'Hidden' ";
            } else {
                message = message + hand[i][0] + hand[i][1] + " ";
            }
        }

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JLabel(message), BorderLayout.NORTH);
        messagePanel.add(imagePanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(null, messagePanel, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static boolean hitStandPane(String message, String title, String[][] hand) {
        // Panel for images
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));

        // Define the target size for the images
        int targetWidth = 230;
        int targetHeight = 350;

        for (String[] card : hand) {
            String imagePath = "/ui/cards/" + card[0] + card[1] + ".png";

            try {
                // Load the image
                ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource(imagePath)));
                Image originalImage = originalIcon.getImage();

                // Resize the image
                Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImage);

                // Add the resized image to the panel
                imagePanel.add(new JLabel(resizedIcon));
                imagePanel.add(Box.createHorizontalStrut(10)); // Add some space between images

            } catch (NullPointerException e) {
                System.err.println("Resource not found: " + imagePath);
                e.printStackTrace();
            }
        }

        // Message
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JLabel(message), BorderLayout.NORTH);
        messagePanel.add(imagePanel, BorderLayout.CENTER);

        // Define custom button texts
        Object[] options = { "Hit", "Stand" };

        // Show the confirm dialog
        int result = JOptionPane.showOptionDialog(null, messagePanel, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        return result == 0; // Return true if "Hit" is selected, false if "Stand" is selected
    }
    
    // GAME

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

    // Show Hand - Terminal
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
        String playerName = messageInput("Welcome to Blackjack Java! \nplease enter your name", "BlackJack Java",
                "/ui/banner.png");

        // Check Cancel Button
        if (playerName != null) {
            showMessage("Good luck " + playerName + "! \nLet’s make this hand a winner!",
                    "Ready to Hit the Jackpot?",
                    "/ui/myIcon.gif");

            int playerChips = 2500;
            int playerBet = 0;

            // Game Loop
            while (playerChips > 0) {

                while (true) {
                    String input = messageInput(
                            "Let’s see your bet, " + playerName
                                    + ".\nHow many chips are you risking this time? \n\nYour chips: " + playerChips,
                            "Make Your Move!", "/ui/myIcon.gif");
                    try {
                        playerBet = Integer.valueOf(input);
                        if (playerBet <= playerChips && playerBet > 0) {
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Looks like you're running low, " + playerName
                                            + ". Please enter a valid bet to continue.",
                                    "Oops, Not Enough Chips", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "That’s not quite right. Please enter a valid number to continue with the game.",
                                "Oops! Wrong Input", JOptionPane.ERROR_MESSAGE);
                    }
                }

                String[][] deck = createDeck();
                shuffleDeck(deck);

                String[][] playerHand = { deck[0], deck[2] };
                String[][] dealerHand = { deck[1], deck[3] };

                int deckIndex = 3;

                showHandPane("Dealer Hand: " + cardToValue(dealerHand[0][0]) + " +  ?", dealerHand, true);

                // Player's turn, Decide (Hit or Stand) - UI
                Boolean playerTurn = true;
                while (playerTurn) {
                    Boolean hit = hitStandPane("Your hand: " + handToValue(playerHand),
                            "Chips: " + playerChips + " | Bet: " + playerBet, playerHand);
                    if (hit) {
                        // Update deck index
                        deckIndex++;

                        playerHand = addCardToHand(playerHand, deck, deckIndex);

                        // Check if player reached 21
                        if (handToValue(playerHand) > 21) {
                            showHandPane("Your Hand:  " + handToValue(playerHand), playerHand, false);
                            JOptionPane.showMessageDialog(null, "You Lost", "BlackJack Java",
                                    JOptionPane.ERROR_MESSAGE);
                            playerTurn = false;
                            break;
                        }
                    } else {
                        playerTurn = false;
                    }
                }

                // Dealer's turn

                // Show Full Hand
                showHandPane("Dealer Hand: " + handToValue(dealerHand), dealerHand, false);

                // Dealer will hit till value > 17
                while (handToValue(dealerHand) < 17) {
                    // Update deck index
                    deckIndex++;
                    dealerHand = addCardToHand(dealerHand, deck, deckIndex);

                    // Show Dealer just oen if doesn't lost yet
                    if(handToValue(playerHand) <= 21) {
                        showHandPane("Dealer Hand: " + handToValue(dealerHand), dealerHand, false);
                    }
                }
                // Show Dealer just oen time if player lost before
                if(handToValue(playerHand) > 21) {
                    showHandPane("Dealer Hand: " + handToValue(dealerHand), dealerHand, false);
                }

                // Hand Values Count
                int playerTotal = handToValue(playerHand);
                int dealerTotal = handToValue(dealerHand);

                // Resume
                JOptionPane.showMessageDialog(null,
                        "Game Results:\n\n" + playerName + " hand: " + playerTotal
                                + "\nDealer’s hand: " + dealerTotal,
                        "Game Results",
                        JOptionPane.INFORMATION_MESSAGE);

                // Set Winner or Tie :)
                if (playerTotal > 21) {
                    JOptionPane.showMessageDialog(null, "You Lost!\n\nDealer Wins", "BlackJack Java - Resume",
                            JOptionPane.INFORMATION_MESSAGE);
                    playerChips = playerChips - playerBet;
                } else if (dealerTotal > 21 || playerTotal > dealerTotal) {
                    JOptionPane.showMessageDialog(null, "You Win!", "BlackJack Java - Resume",
                            JOptionPane.INFORMATION_MESSAGE);
                    playerChips = playerChips + playerBet;
                } else if (playerTotal < dealerTotal) {
                    JOptionPane.showMessageDialog(null, "You Lost!\n\nDealer Wins", "BlackJack Java - Resume",
                            JOptionPane.INFORMATION_MESSAGE);
                    playerChips = playerChips - playerBet;
                } else {
                    JOptionPane.showMessageDialog(null, "Tie!", "BlackJack Java - Resume",
                            JOptionPane.INFORMATION_MESSAGE);
                    playerChips = playerChips - playerBet;
                    playerChips = playerChips + (int) Math.ceil(playerBet / 2.0);
                }

                // Break if player has no chips
                if (playerChips <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "Oh no, " + playerName + "! You've lost all your chips. Better luck next time!",
                            "Game Over!",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }

                // Player can continue if chips > 0
                int resume = optionInput(
                        "The game’s over, " + playerName
                                + "! \n\nDo you want to try your luck again and play another round?",
                        "Ready for Another Round?",
                        "/ui/myIcon.gif");

                if (resume != 0) {
                    break;
                }
            }
            if (playerChips > 0) {
                JOptionPane.showMessageDialog(null,
                        "Your chip balance is now: " + playerChips + ", " + playerName
                                + ".\n\nThanks for playing—hope to see you again soon!",
                        "Chip Update",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Thanks for playing! \nWe hope you had fun. \nLook forward to seeing you in the next game!", "Farewell for Now!", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}