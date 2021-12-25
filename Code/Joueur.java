import java.util.Scanner;

public class Joueur {
	
	private ChampMine c = new ChampMine();
	private boolean perdu;
	Scanner s = new Scanner(System.in);
	
	public void jouer(){
		
		this.perdu = false;
		c.nbCasesRevelees = 0;
		c.effacerConsole();
		
		System.out.println("Le jeu du demineur -- code par Alexis REIS, Aymane GHANAM & Valentin CORBIC");
		System.out.println("-- Version 5 du 31/05");
		System.out.println("\nLe demineur est un jeu de reflexion dont le but est de localiser des mines\ncachees dans un champ virtuel avec pour seule indication le nombre de mines\ndans les zones adjacentes.");

		// Choix de la difficulté, puis on genere un champ
        this.choixDifficulte(c);
		c.champ = c.genererChamp(c.N, c.M);
		c.nbDrapeauxRestants = c.M;
		c.premierCoup();
			
		// DEBUT DE LA PARTIE
		while(!this.aGagne(c) && !this.perdu){
			
			c.effacerConsole();
			c.afficherDemineur();
			this.actionJoueur(c);
		}
		
		// FIN DE LA PARTIE 
		c.effacerConsole();
		if (this.aGagne(c)){
			c.afficherChamp();
			System.out.println("> Tu es un pro ! Avec toi, le terrain est sans danger! :D");
		}else if(this.perdu){
		    c.afficherChamp();
			System.out.println("> BOOM ! Tu as perdu :/ Rejoues pour te venger!");
		}
		// RECOMMENCER UNE PARTIE
		System.out.print("\n> Veux-tu recommencer ? Si oui, tapes 1 :");
		int action = s.nextInt();
		if(action == 1){
			this.jouer();
		}
		
	}
	
	private void choixDifficulte(ChampMine c){
		
		System.out.println("\n> On est sympa, on te laisse choisir la difficulte ! Taper :\n  - 1 : facile (champ 10x10 et 10 mines) \n  - 2 : intermediaire (champ 10x10 et 20 mines)\n  - 3 : difficile (champ 15x15 et 50 mines)\n  - 4 : personnalise");
		int niveau = s.nextInt();
		while(niveau < 1 || niveau > 4){
			System.out.println("> Es-tu bien sur de savoir compter jusqu'a 4 ? Prouves-le moi !");
			niveau = s.nextInt();
		}
		
		switch (niveau){
			case 1 :
				c.N = 10;
				c.M = 10;
				break;
			case 2 :
				c.N = 10;
				c.M = 20;
				break;
			case 3 :
				c.N = 15;
				c.M = 50;
				break;
			case 4 :
				System.out.println("> Tapes la taille du champ voulue (entre 5 et 20):");
				c.N = s.nextInt();
				while(c.N<5 || c.N > 20){
					System.out.println("> Il y a, je le crains, une erreur dans la matrice. Veux-tu bien retaper ?");
					c.N = s.nextInt();
				}
				
				System.out.println("> Tapes le nombre de mines a dissimuler dans ce champ (entre 1 et " + (int)(0.5*c.N*c.N) + ") :");
				c.M = s.nextInt();
				while(c.M<1 || c.M > (int)(0.5*c.N*c.N)){
					System.out.println("> Es-tu bien sur de vouloir cela ? Je pense que non, essaies encore : ");
					c.M = s.nextInt();
				}
				break;
		}
	}
	
	private void actionJoueur(ChampMine c){
		
		System.out.println("\n> Que souhaites-tu faire ? Tapes : " + "\n  - 1 pour miner\n  - 2 pour poser un drapeau\n  - 3 pour retirer un drapeau");
		int action = s.nextInt();
		while(action<1 || action > 3){
			System.out.println("> C'est pas croyable, 1 2 et 3 c'est pourtant pas bien complique ! Tu veux bien reessayer ?");
			action = s.nextInt();
		}			
					    
		switch(action){
			case 1 : 
				this.miner(c);
				break;
			case 2 : 
				if(c.nbDrapeauxRestants > 0){
					this.planterDrapeau(c);
				} else{
					System.out.println("> T'essairais pas me rouler par hasard? Tu n'as plus de drapeaux !");
					this.actionJoueur(c);
				}
                break;
			case 3 : 
				if(c.nbDrapeauxRestants<c.M){
					this.retirerDrapeau(c);
				} else{
					System.out.println("Malheureusement il n'y a aucun drapeau en vue...");
					this.actionJoueur(c);
				}
				break;
                
		}
	}
	
	// CHOIX CASE VALIDE
	private int[] choisirCase(ChampMine c){
		int[] choix = new int[2];
		System.out.println("> Quel case choisis-tu ? (entre le numero de la ligne, appuie sur entree, puis entre le numero de la colonne)");
		choix[0] = s.nextInt() - 1;
		choix[1] = s.nextInt() - 1;
		while(choix[0] < 0 || choix[0] > c.N || choix[1] < 0 || choix[1] > c.N){
			System.out.println("\n> Erreur. La case choisie n'existe pas je le crains...");
			System.out.println("> Quel case chosis-tu ? (entre le numero de la ligne, appuie sur entree, puis entre le numero de la colonne)");
			choix[0] = s.nextInt() - 1;
			choix[1] = s.nextInt() - 1;
	    }
		return choix;
	}
	
	private void miner(ChampMine c){
		System.out.println("\n> MODE MINEUR : danger, terrain non securise");
		int[] choix = choisirCase(c);
		while(c.demineur[choix[0]][choix[1]] != '.'){
			System.out.println("Il me semble que tu connaisses deja le terrain... Tu veux pas aller ailleurs ?");
			choix = choisirCase(c);
		}
        if(c.champ[choix[0]][choix[1]]==true){
		    this.perdu = true;
	    } else{
		    c.remplaceDemineur(choix[0], choix[1]);
        }
    }
    
    private void planterDrapeau(ChampMine c){
		
		System.out.println("> MODE PLANTER DRAPEAU : mission on a marche sur la lune");
		int[] choix = choisirCase(c);
		while(c.demineur[choix[0]][choix[1]] != '.'){
			System.out.println("C'est deja securise chef! Plantes ton drapeau ailleurs :");
			choix = choisirCase(c);
		}
		c.demineur[choix[0]][choix[1]] ='P';
		c.nbDrapeauxRestants --;
       
    }
    
    private void retirerDrapeau(ChampMine c){
		
		System.out.println("> MODE RETIRER DRAPEAU : la terre c'est pas si mal finalement");
		int[] choix = choisirCase(c);
		while(c.demineur[choix[0]][choix[1]] != 'P'){
			System.out.println("C'est pas un drapeau qu'il y a ici mousaillon ! Essaye encore :");
			choix = choisirCase(c);
		}
        c.demineur[choix[0]][choix[1]] ='.';
		c.nbDrapeauxRestants ++;
        
    }
    	
	// Le joueur gagne lorsque toutes les cases qui ne sont pas des mines sont revelees
	private boolean aGagne(ChampMine c){
		return (c.N*c.N-c.M-c.nbCasesRevelees)==0;
	}
		
}

