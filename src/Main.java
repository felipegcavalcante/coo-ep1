import java.awt.Color;

public class Main {

	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */

	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;


	/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */

	public static void busyWait(long time){

		while(System.currentTimeMillis() < time) Thread.yield();
	}

	/* Encontra e devolve o primeiro índice do  */
	/* array referente a uma posição "inativa". */

	public static int findFreeIndex(int [] stateArray){

		int i;

		for(i = 0; i < stateArray.length; i++){

			if(stateArray[i] == INACTIVE) break;
		}

		return i;
	}

	public static int findFreeIndexProjectiles(Projectile [] stateArray){

		int i;

		for(i = 0; i < stateArray.length; i++){
			if(stateArray[i].getState() == State.INACTIVE) break;
		}

		return i;
	}

	/* Encontra e devolve o conjunto de índices (a quantidade */
	/* de índices é defnida através do parâmetro "amount") do */
	/* array, referentes a posições "inativas".               */

	public static int [] findFreeIndex(int [] stateArray, int amount){

		int i, k;
		int [] freeArray = { stateArray.length, stateArray.length, stateArray.length };

		for(i = 0, k = 0; i < stateArray.length && k < amount; i++){

			if(stateArray[i] == INACTIVE) {

				freeArray[k] = i;
				k++;
			}
		}

		return freeArray;
	}

	public static int [] findFreeIndex(Projectile [] stateArray, int amount) {

		int i, k;
		int [] freeArray = { stateArray.length, stateArray.length, stateArray.length };

		for(i = 0, k = 0; i < stateArray.length && k < amount; i++){

			if(stateArray[i].getState() == State.INACTIVE) {

				freeArray[k] = i;
				k++;
			}
		}

		return freeArray;
	}

	/* Método principal */
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */

		long delta;
		long currentTime = System.currentTimeMillis();

		Player player = new Player(
				State.ACTIVE,
				new Position(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90),
				new Speed(0.25, 0.25),
				12.0,
				currentTime,
				new Explosion(0, 0)
		);

		/* variáveis dos projéteis disparados pelo player */
		Projectile[] playerProjectiles = new Projectile[10];


		/* variáveis dos inimigos tipo 1 */
//		Enemy1 enemy1[] = new Enemy1[10];
		int [] enemy1_states = new int[10];						// estados
		double [] enemy1_X = new double[10];					// coordenadas x
		double [] enemy1_Y = new double[10];					// coordenadas y
		double [] enemy1_V = new double[10];					// velocidades
		double [] enemy1_angle = new double[10];				// ângulos (indicam direção do movimento)
		double [] enemy1_RV = new double[10];					// velocidades de rotação
		double [] enemy1_explosion_start = new double[10];		// instantes dos inícios das explosões
		double [] enemy1_explosion_end = new double[10];		// instantes dos finais da explosões
		long [] enemy1_nextShoot = new long[10];				// instantes do próximo tiro
		double enemy1_radius = 9.0;								// raio (tamanho do inimigo 1)
		long nextEnemy1 = currentTime + 2000;					// instante em que um novo inimigo 1 deve aparecer

		/* variáveis dos inimigos tipo 2 */

//		Enemy2 enemy2[] = new Enemy2[10];
		int [] enemy2_states = new int[10];						// estados
		double [] enemy2_X = new double[10];					// coordenadas x
		double [] enemy2_Y = new double[10];					// coordenadas y
		double [] enemy2_V = new double[10];					// velocidades
		double [] enemy2_angle = new double[10];				// ângulos (indicam direção do movimento)
		double [] enemy2_RV = new double[10];					// velocidades de rotação
		double [] enemy2_explosion_start = new double[10];		// instantes dos inícios das explosões
		double [] enemy2_explosion_end = new double[10];		// instantes dos finais das explosões
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do próximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formação de voo")
		double enemy2_radius = 12.0;							// raio (tamanho aproximado do inimigo 2)
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer

		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		Projectile enemyProjectiles[] = new Projectile[200];
//		int [] e_projectile_states = new int[200];				// estados
//		double [] e_projectile_X = new double[200];				// coordenadas x
//		double [] e_projectile_Y = new double[200];				// coordenadas y
//		double [] e_projectile_VX = new double[200];			// velocidade no eixo x
//		double [] e_projectile_VY = new double[200];			// velocidade no eixo y
		double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)

		/* estrelas que formam o fundo de primeiro plano */
		double [] background1_X = new double[20];
		double [] background1_Y = new double[20];
		double background1_speed = 0.070;
		double background1_count = 0.0;

		/* estrelas que formam o fundo de segundo plano */
		double [] background2_X = new double[50];
		double [] background2_Y = new double[50];
		double background2_speed = 0.045;
		double background2_count = 0.0;

		/* inicializações */
//		for(int i = 0; i < projectile_states.length; i++) projectile_states[i] = INACTIVE;
		for (int i = 0; i < playerProjectiles.length; i++) {
			playerProjectiles[i] = new Projectile(
					new Position(0, 0),
					new Speed(0, 0),
					2.0
			);
		}

//		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		for (int i = 0; i < enemyProjectiles.length; i++) {
			enemyProjectiles[i] = new Projectile(
					new Position(0, 0),
					new Speed(0, 0),
					2.0
			);
		}

		for(int i = 0; i < enemy1_states.length; i++) enemy1_states[i] = INACTIVE;
//		for (int i = 0; i < enemy1.length; i++) {
//			enemy1[i] = new Enemy1(
//					new Position(0, 0),
//					new Speed(0, 0),
//					0,
//					new Explosion(0, 0),
//					currentTime + 2000
//			);
//		}

		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
//		for (int i = 0; i < enemy2.length; i++) {
//			enemy2[i] = new Enemy2(
//					new Position(0, 0),
//					new Speed(0, 0),
//					0,
//					new Explosion(0, 0),
//					currentTime + 7000
//			);
//		}

		for(int i = 0; i < background1_X.length; i++){

			background1_X[i] = Math.random() * GameLib.WIDTH;
			background1_Y[i] = Math.random() * GameLib.HEIGHT;
		}

		for(int i = 0; i < background2_X.length; i++){

			background2_X[i] = Math.random() * GameLib.WIDTH;
			background2_Y[i] = Math.random() * GameLib.HEIGHT;
		}

		/* iniciado interface gráfica */

		GameLib.initGraphics();

		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/*                                                                                               */
		/* O main loop do jogo possui executa as seguintes operações:                                    */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu desde a última atualização     */
		/*    e no timestamp atual: posição e orientação, execução de disparos de projéteis, etc.        */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/

		while(running){

			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projéteis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a última atualização.             */
			delta = System.currentTimeMillis() - currentTime;

			/* Já a variável "currentTime" nos dá o timestamp atual.  */
			currentTime = System.currentTimeMillis();

			/***************************/
			/* Verificação de colisões */
			/***************************/

			if(player.getState() == State.ACTIVE){

				/* colisões player - projeteis (inimigo) */
				for(int i = 0; i < enemyProjectiles.length; i++){
					Projectile enemyProjectile = enemyProjectiles[i];

					double dx = enemyProjectile.getPositionX() - player.getPosition().getX();
					double dy = enemyProjectile.getPositionY() - player.getPosition().getY();
					double dist = Math.sqrt(dx * dx + dy * dy);

					if(dist < (player.getRadius() + e_projectile_radius) * 0.8){
						player.explode(currentTime);
					}
				}

				/* colisões player - inimigos */
				for(int i = 0; i < enemy1_states.length; i++){

					double dx = enemy1_X[i] - player.getPosition().getX();
					double dy = enemy1_Y[i] - player.getPosition().getY();
					double dist = Math.sqrt(dx * dx + dy * dy);

					if(dist < (player.getRadius() + enemy1_radius) * 0.8){
						player.explode(currentTime);
					}
				}

				for(int i = 0; i < enemy2_states.length; i++){
					double dx = enemy2_X[i] - player.getPosition().getX();
					double dy = enemy2_Y[i] - player.getPosition().getY();
					double dist = Math.sqrt(dx * dx + dy * dy);

					if(dist < (player.getRadius() + enemy2_radius) * 0.8){
						player.explode(currentTime);
					}
				}
			}

			/* colisões projeteis (player) - inimigos */

			for(int k = 0; k < playerProjectiles.length; k++){

				for(int i = 0; i < enemy1_states.length; i++){

					if(enemy1_states[i] == ACTIVE){

						double dx = enemy1_X[i] - playerProjectiles[k].getPositionX();
						double dy = enemy1_Y[i] - playerProjectiles[k].getPositionY();
						double dist = Math.sqrt(dx * dx + dy * dy);

						if(dist < enemy1_radius){

							enemy1_states[i] = EXPLODING;
							enemy1_explosion_start[i] = currentTime;
							enemy1_explosion_end[i] = currentTime + 500;

//							enemy1[i].explode();
						}
					}
				}

				for(int i = 0; i < enemy2_states.length; i++){

					if(enemy2_states[i] == ACTIVE){

						double dx = enemy2_X[i] - playerProjectiles[k].getPositionX();
						double dy = enemy2_Y[i] - playerProjectiles[k].getPositionY();
						double dist = Math.sqrt(dx * dx + dy * dy);

						if(dist < enemy2_radius){

							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;

//							enemy2[i].explode();
						}
					}
				}
			}

			/***************************/
			/* Atualizações de estados */
			/***************************/

			/* projeteis (player) */

			for(int i = 0; i < playerProjectiles.length; i++) {
				Projectile projectile = playerProjectiles[i];

				if(!projectile.isActive()){
					continue;
				}

				/* verificando se projétil saiu da tela */
				if(projectile.getPositionY() < 0) {
					projectile.deactivate();
					continue;
				}

				projectile.increasePositionX(projectile.getSpeed().getSpeedX() * delta);
				projectile.increasePositionY(projectile.getSpeed().getSpeedY() * delta);
			}

			/* projeteis (inimigos) */

			for(int i = 0; i < enemyProjectiles.length; i++){
				Projectile enemyProjectile = enemyProjectiles[i];

				if(enemyProjectile.isActive()){

					/* verificando se projétil saiu da tela */
					if(enemyProjectile.getPositionY() > GameLib.HEIGHT) {

						enemyProjectile.deactivate();
					}
					else {

						enemyProjectile.increasePositionX(enemyProjectile.getSpeed().getSpeedX() * delta);
						enemyProjectile.increasePositionY(enemyProjectile.getSpeed().getSpeedY() * delta);
					}
				}
			}

			/* inimigos tipo 1 */

			for(int i = 0; i < enemy1_states.length; i++){

				if(enemy1_states[i] == EXPLODING){

					if(currentTime > enemy1_explosion_end[i]){

						enemy1_states[i] = INACTIVE;
					}
				}

				if(enemy1_states[i] == ACTIVE){

					/* verificando se inimigo saiu da tela */
					if(enemy1_Y[i] > GameLib.HEIGHT + 10) {

						enemy1_states[i] = INACTIVE;
					}
					else {

						enemy1_X[i] += enemy1_V[i] * Math.cos(enemy1_angle[i]) * delta;
						enemy1_Y[i] += enemy1_V[i] * Math.sin(enemy1_angle[i]) * delta * (-1.0);
						enemy1_angle[i] += enemy1_RV[i] * delta;

						if(currentTime > enemy1_nextShoot[i] && enemy1_Y[i] < player.getPosition().getY()){

							int free = findFreeIndexProjectiles(enemyProjectiles);

							if(free < enemyProjectiles.length){
								Projectile enemyProjectile = enemyProjectiles[free];

								enemyProjectile.setPosition(enemy1_X[i], enemy1_Y[i]);
								enemyProjectile.setSpeed(
									Math.cos(enemy1_angle[i]) * 0.45,
									Math.sin(enemy1_angle[i]) * 0.45 * (-1.0)
								);
								enemyProjectile.setState(State.ACTIVE);

								enemy1_nextShoot[i] = (long) (currentTime + 200 + Math.random() * 500);
							}
						}
					}
				}
			}

			/* inimigos tipo 2 */

			for(int i = 0; i < enemy2_states.length; i++){

				if(enemy2_states[i] == EXPLODING){

					if(currentTime > enemy2_explosion_end[i]){

						enemy2_states[i] = INACTIVE;
					}
				}

				if(enemy2_states[i] == ACTIVE){

					/* verificando se inimigo saiu da tela */
					if(	enemy2_X[i] < -10 || enemy2_X[i] > GameLib.WIDTH + 10 ) {

						enemy2_states[i] = INACTIVE;
					}
					else {

						boolean shootNow = false;
						double previousY = enemy2_Y[i];

						enemy2_X[i] += enemy2_V[i] * Math.cos(enemy2_angle[i]) * delta;
						enemy2_Y[i] += enemy2_V[i] * Math.sin(enemy2_angle[i]) * delta * (-1.0);
						enemy2_angle[i] += enemy2_RV[i] * delta;

						double threshold = GameLib.HEIGHT * 0.30;

						if(previousY < threshold && enemy2_Y[i] >= threshold) {

							if(enemy2_X[i] < GameLib.WIDTH / 2) enemy2_RV[i] = 0.003;
							else enemy2_RV[i] = -0.003;
						}

						if(enemy2_RV[i] > 0 && Math.abs(enemy2_angle[i] - 3 * Math.PI) < 0.05){

							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 3 * Math.PI;
							shootNow = true;
						}

						if(enemy2_RV[i] < 0 && Math.abs(enemy2_angle[i]) < 0.05){

							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 0.0;
							shootNow = true;
						}

						if(shootNow){

							double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
							int [] freeArray = findFreeIndex(enemyProjectiles, angles.length);

							for(int k = 0; k < freeArray.length; k++){

								int free = freeArray[k];

								if(free < enemyProjectiles.length){
									Projectile projectile = enemyProjectiles[free];

									double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;

									projectile.setPosition(enemy2_X[i], enemy2_Y[i]);
									projectile.setSpeed(Math.cos(a) * 0.30, Math.sin(a) * 0.30);
									projectile.setState(State.ACTIVE);
								}
							}
						}
					}
				}
			}

			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */

			if(currentTime > nextEnemy1){

				int free = findFreeIndex(enemy1_states);

				if(free < enemy1_states.length){

					enemy1_X[free] = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
					enemy1_Y[free] = -10.0;
					enemy1_V[free] = 0.20 + Math.random() * 0.15;
					enemy1_angle[free] = 3 * Math.PI / 2;
					enemy1_RV[free] = 0.0;
					enemy1_states[free] = ACTIVE;
					enemy1_nextShoot[free] = currentTime + 500;
					nextEnemy1 = currentTime + 500;
				}
			}

			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */

			if(currentTime > nextEnemy2){

				int free = findFreeIndex(enemy2_states);

				if(free < enemy2_states.length){

					enemy2_X[free] = enemy2_spawnX;
					enemy2_Y[free] = -10.0;
					enemy2_V[free] = 0.42;
					enemy2_angle[free] = (3 * Math.PI) / 2;
					enemy2_RV[free] = 0.0;
					enemy2_states[free] = ACTIVE;

					enemy2_count++;

					if(enemy2_count < 10){

						nextEnemy2 = currentTime + 120;
					}
					else {

						enemy2_count = 0;
						enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
						nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
					}
				}
			}

			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(player.getState() == State.EXPLODING){

				if(currentTime > player.getExplosion().getEnd()){

					player.activate();
				}
			}

			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/

			if(player.getState() == State.ACTIVE){

				if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.setPositionY(player.getPosition().getY() - delta * player.getSpeed().getSpeedY());
				if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.setPositionY(player.getPosition().getY() + delta * player.getSpeed().getSpeedY());
				if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.setPositionX(player.getPosition().getX() - delta * player.getSpeed().getSpeedX());
				if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.setPositionX(player.getPosition().getX() + delta * player.getSpeed().getSpeedY());
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {

					if(currentTime > player.getNextShot()){

						int free = findFreeIndexProjectiles(playerProjectiles);

						if(free < playerProjectiles.length){
							Projectile projectile = playerProjectiles[free];

							projectile.setPosition(
								player.getPosition().getX(),
								player.getPosition().getY() - 2 * player.getRadius()
							);
							projectile.setSpeed(0.0, -1.0);
							projectile.setState(State.ACTIVE);
							player.setNextShot(currentTime + 100);
						}
					}
				}
			}

			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;

			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */

			if(player.getPosition().getX() < 0.0) player.setPositionX(0.0);
			if(player.getPosition().getX() >= GameLib.WIDTH) player.setPositionX(GameLib.WIDTH - 1);
			if(player.getPosition().getY() < 25.0) player.setPositionY(25.0);
			if(player.getPosition().getY() >= GameLib.HEIGHT) player.setPositionY(GameLib.HEIGHT - 1);

			/*******************/
			/* Desenho da cena */
			/*******************/

			/* desenhando plano fundo distante */

			GameLib.setColor(Color.DARK_GRAY);
			background2_count += background2_speed * delta;

			for(int i = 0; i < background2_X.length; i++){

				GameLib.fillRect(background2_X[i], (background2_Y[i] + background2_count) % GameLib.HEIGHT, 2, 2);
			}

			/* desenhando plano de fundo próximo */

			GameLib.setColor(Color.GRAY);
			background1_count += background1_speed * delta;

			for(int i = 0; i < background1_X.length; i++){

				GameLib.fillRect(background1_X[i], (background1_Y[i] + background1_count) % GameLib.HEIGHT, 3, 3);
			}

			/* desenhando player */

			if(player.getState() == State.EXPLODING){

				double alpha = (currentTime - player.getExplosion().getStart()) / (player.getExplosion().getEnd() - player.getExplosion().getStart());
				GameLib.drawExplosion(player.getPosition().getX(), player.getPosition().getY(), alpha);
			}
			else{

				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(player.getPosition().getX(), player.getPosition().getY(), player.getRadius());
			}


			/* deenhando projeteis (player) */

			for(int i = 0; i < playerProjectiles.length; i++){
				Projectile projectile = playerProjectiles[i];

				if(projectile.isActive()){
					GameLib.setColor(Color.GREEN);
					GameLib.drawLine(projectile.getPositionX(), projectile.getPositionY() - 5, projectile.getPositionX(), projectile.getPositionY() + 5);
					GameLib.drawLine(projectile.getPositionX() - 1, projectile.getPositionY() - 3, projectile.getPositionX() - 1, projectile.getPositionY() + 3);
					GameLib.drawLine(projectile.getPositionX() + 1, projectile.getPositionY() - 3, projectile.getPositionX() + 1, projectile.getPositionY() + 3);
				}
			}

			/* desenhando projeteis (inimigos) */

			for(int i = 0; i < enemyProjectiles.length; i++){
				Projectile projectile = enemyProjectiles[i];

				if(projectile.isActive()){

					GameLib.setColor(Color.RED);
					GameLib.drawCircle(projectile.getPositionX(), projectile.getPositionY(), e_projectile_radius);
				}
			}

			/* desenhando inimigos (tipo 1) */

			for(int i = 0; i < enemy1_states.length; i++){

				if(enemy1_states[i] == EXPLODING){

					double alpha = (currentTime - enemy1_explosion_start[i]) / (enemy1_explosion_end[i] - enemy1_explosion_start[i]);
					GameLib.drawExplosion(enemy1_X[i], enemy1_Y[i], alpha);
				}

				if(enemy1_states[i] == ACTIVE){

					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy1_X[i], enemy1_Y[i], enemy1_radius);
				}
			}

			/* desenhando inimigos (tipo 2) */

			for(int i = 0; i < enemy2_states.length; i++){

				if(enemy2_states[i] == EXPLODING){

					double alpha = (currentTime - enemy2_explosion_start[i]) / (enemy2_explosion_end[i] - enemy2_explosion_start[i]);
					GameLib.drawExplosion(enemy2_X[i], enemy2_Y[i], alpha);
				}

				if(enemy2_states[i] == ACTIVE){

					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy2_X[i], enemy2_Y[i], enemy2_radius);
				}
			}

			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */

			GameLib.display();

			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */

			busyWait(currentTime + 5);
		}

		System.exit(0);
	}
}
