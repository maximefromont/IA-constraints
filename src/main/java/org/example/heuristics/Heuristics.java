package org.example.heuristics;

import org.example.util.Node;
import org.example.EscampeBoard;

public class Heuristics {
    // Constantes pour les scores
    private static final int DIRECT_WIN_SCORE = 1000;
    private static final int DIRECT_LOSE_SCORE = 1000;
    private static final int WIN_SCORE = 100;
    private static final int LOSE_SCORE = 100;
    private static final int CANT_MOVE_SCORE = 100;

    // Coefficients de pondération pour chaque heuristique
    private static final double DIRECT_OUTCOME_WEIGHT = 1.0;
    private static final double GAME_OUTCOME_WEIGHT = 0.8;
    private static final double MOBILITY_WEIGHT = 0.5;
    private static final double MOBILITY_RESTRICTION_WEIGHT = 0.7;

    public static int mobilityHeuristic(EscampeBoard board, String player) {
        String opponent = player.equals("noir") ? "blanc" : "noir";
        return board.possiblesMoves(player).length - board.possiblesMoves(opponent).length;
    }

    public static int directOutcomeHeuristic(Node node) {
        if (node.getBoard().gameOver()) {
            if (node.getDepth() == 1) {
                return DIRECT_WIN_SCORE;
            } else if (node.getDepth() == 2) {
                return DIRECT_LOSE_SCORE;
            }
        }
        return 0;
    }

    public static int gameOutcomeHeuristic(Node node) {
        if (node.getDepth() >= 3 && node.getBoard().gameOver()) {
            if (node.getBoard().getWinner().equals(node.getFriend())) {
                return WIN_SCORE;
            } else if (node.getBoard().getWinner().equals(node.getEnnemy())) {
                return LOSE_SCORE;
            }
        }
        return 0;
    }

    public static int mobilityRestrictionHeuristic(Node node) {
        if (node.getBoard().possiblesMoves(node.getEnnemy()).length == 0) {
            return CANT_MOVE_SCORE;
        } else if (node.getBoard().possiblesMoves(node.getFriend()).length == 0) {
            return CANT_MOVE_SCORE;
        }
        return 0;
    }

    public static int HeuristicValue(Node node, String player) {
        int directOutcomeValue = directOutcomeHeuristic(node);
        int gameOutcomeValue = gameOutcomeHeuristic(node);
        int mobilityValue = mobilityHeuristic(node.getBoard(), player);
        int mobilityRestrictionValue = mobilityRestrictionHeuristic(node);

        return (int) (DIRECT_OUTCOME_WEIGHT * directOutcomeValue
                + GAME_OUTCOME_WEIGHT * gameOutcomeValue
                + MOBILITY_WEIGHT * mobilityValue
                + MOBILITY_RESTRICTION_WEIGHT * mobilityRestrictionValue);
    }
}

