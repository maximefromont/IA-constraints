#!/bin/bash

# Démarrer le serveur de jeu
(cd ~/Cour/résolution\ de\ contrainte/projet && java -cp escampeobf.jar escampe.ServeurJeu 1234 &)

# Attendre que le serveur soit prêt
sleep 5

# Démarrer le client de jeu JoueurAleatoire
(cd ~/Cour/résolution\ de\ contrainte/projet/IA-constraints/out/artifacts/app && java -cp IA-constraints.jar org.example.ClientJeu org.example.JoueurAleatoire localhost 1234 &)

# Démarrer le client de jeu JoueurIntelligent
(cd ~/Cour/résolution\ de\ contrainte/projet/IA-constraints/out/artifacts/app && java -cp IA-constraints.jar org.example.ClientJeu org.example.JoueurIntelligent localhost 1234 &)
