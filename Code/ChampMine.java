public class ChampMine {
	
	public static int N;					// taille du champ de mines
	public static int M;					// nombre de mines
	public boolean[][] champ;				// le champ de mine, true si il y a une mine, false sinon
	public char[][] demineur;				// le champ de mine visible par le joueur = le demineur
	public int nbCasesRevelees;				// le nombre de cases revelees par le joueur
	public int nbDrapeauxRestants;			// le nombre de drapeau qu'il peut encore placer sur le demineur
	
	// Genere un champ de mine aléatoire avec un nombre de mines défini
	public boolean [][] genererChamp(int N, int M) {

        boolean[][] champ = new boolean[N][N];
        int nbMinesPosees = 0;
        while (nbMinesPosees<M){
			
			int l = (int)(Math.random()*N);
			int c = (int)(Math.random()*N);
			
			if(!champ[l][c]){
				champ[l][c] = true;
				nbMinesPosees ++;
			}			
		}
		
		// On initialise aussi le demineur qui a la meme taille que le champ cree
		this.demineur = new char[N][N];
      	for(int i = 0; i<N; i++){
			for(int j = 0; j<N; j++){
				this.demineur[i][j] = '.';
			}
		}
		     
        return champ;
    }
    
    // AFFICHAGE DE LA REPONSE : On remplace toutes les mines par le caractere 'X' à leur emplacement dans le demineur
    public void afficherChamp(){
		for(int i = 0; i<this.N; i++){
			for(int j = 0; j<this.N; j++){
				if(this.champ[i][j]){
					this.demineur[i][j] = 'X';
				}
			}
		}
		this.afficherDemineur();
	}
	
	// VISUEL JOUEUR : ce à quoi le joueur à accès et interagit en conséquence
	public void afficherDemineur(){
		// Affichage des colonnes
		System.out.print("      1 |");
		for(int i = 2; i<=this.N; i++){
			if(i<10){
				System.out.print(" " + i + " |");
			} else{
				System.out.print(" " + i + "|");
			}
		}
		System.out.print("\n-----");
		for(int i = 1; i<=this.N; i++){
			System.out.print("---|");
		}
		System.out.print("\n");
    	for(int i = 0; i<this.N; i++) {
			
			// Affichage des lignes
			if(i<9){
				System.out.print((i+1) + "  || ");
			} else if(i>=9){
				System.out.print((i+1) + " || ");
			}
			
			// Affichage du contenu déminé
        	for(int j = 0; j<this.N; j++) {
            	System.out.print(this.demineur[i][j] + " | ");
            }
          	System.out.println();
        }
        // Affichage des variables utiles
        System.out.println("\n - Nombre de mines dans le champ : " + this.M); 
		System.out.println(" - Drapeaux restants : " + this.nbDrapeauxRestants);
		System.out.println("\n> Tu as complete " + this.score() + " % du champ de mine.");
		
	}
	
	// CALCUL DU SCORE : pourcentage des cases revelees
	public int score(){
		return (int)((100*(double)this.nbCasesRevelees/(this.N*this.N-this.M)));
	}
	
  	// NOMBRE BOMBES ADJACENTES à la case choisie. A utiliser si la case considérée n'est pas une bombe !
	public int nbAdjacents (int l, int c){
    	int nbAdjacents = 0;
    	for (int i=l-1; i<=l+1; i++) {
			for (int j=c-1; j<=c+1; j++) {
				if(i>=0 && i<this.N && j>=0 && j<this.N && this.champ[i][j]) {
					nbAdjacents++;
				}
			}
        }
        return nbAdjacents;
	}
	
	// REMPLACE CASE [l][c] du demineur par son nombre de mines voisines
	public void remplaceDemineur(int l, int c){
		
		// Si la case considérée est pas dans la matrice on sort de la methode
		if(l<0 || l>= this.N || c<0 || c>=N){
			return;
		}
		 
		// Si la case est deja revelee on sort de la methode
		if(this.demineur[l][c] != '.'){
			return;
		}
		
		// Si le nb de mines voisines n'est pas nul on remplace la case du demineur par le nombre de mines voisines
		int nbAdjacents = this.nbAdjacents(l, c);
		
		if(nbAdjacents != 0){
			this.demineur[l][c] = (char)(nbAdjacents + '0');
			nbCasesRevelees ++;
			return;
		} else {
			// Sinon on refait la methode sur toutes les cases voisines pour reveler tous le terrain nul
			this.demineur[l][c] = '0';
			nbCasesRevelees++;
			
			remplaceDemineur(l-1, c+1);
			remplaceDemineur(l-1, c);
			remplaceDemineur(l-1, c-1);
			
			remplaceDemineur(l, c+1);
			remplaceDemineur(l, c-1);
			
			remplaceDemineur(l+1, c+1);
			remplaceDemineur(l+1, c);
			remplaceDemineur(l+1, c-1);
						
		}
	}
		
	// Le premier coup est joue par l'ordinateur pour lancer correctement la partie
	public void premierCoup(){
		
		int l = (int)(Math.random()*this.N);
	    int c = (int)(Math.random()*this.N);
		    
		while((this.nbAdjacents(l,c) != 0) || (this.champ[l][c] == true) ){

			l = (int)(Math.random()*this.N);
			c = (int)(Math.random()*this.N); 
		}
		    
		remplaceDemineur(l,c);
	}
    
    public void effacerConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
