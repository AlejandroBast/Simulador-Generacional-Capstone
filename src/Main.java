public class Main {
  public static void main(String[] args){
    int w = 0;
    int h = 0;
    int g = 0;
    int s = 0;
    int n = 0;
    String m = "";

    int[][] mapa;

    int gConteo = 0;

    for (String arg : args) {
      String[] partes = arg.split("=");

      if (partes.length == 2) {
        String clave = partes[0];
        String valor = partes[1];

        switch (clave) {

          case "w":
            int Ws = Integer.parseInt(valor);
            if (Ws == 5 || Ws == 10 || Ws == 15 ||
                    Ws == 20 || Ws == 40 || Ws == 80) {
              w = Ws;
            } else {
              System.out.println("w invalido");
            }
            break;

          case "h":
            int hs = Integer.parseInt(valor);
            if (hs == 5 || hs == 10 ||
                    hs == 20 || hs == 40 ) {
              h = hs;
            } else {
              System.out.println("h invalido");
            }
            break;

          case "g":
            int gs = Integer.parseInt(valor);
            if (gs > 0 && gs < 1000){
              g = gs;
            } else {
              System.out.println("g invalido");
            }
            break;

          case "s":
            int ss = Integer.parseInt(valor);
            if (ss == 0 || ss == 250 || ss == 500 ||
                    ss == 1000 || ss == 50000 ){
              s = ss;
            } else {
              System.out.println("s invalido");
            }
            break;

          case "n":
            int ns = Integer.parseInt(valor);
            if (ns >= 1 && ns <= 4 ){
              n = ns;
            } else {
              System.out.println("n invalido");
            }
            break;

          case "m":
            m = valor;
            break;
        }
      }
    }

    if (m.equals("rnd")) {
      mapa = generarMapaAleatorio(w, h);
    } else {
      mapa = generarMapaDesdeTexto(m, w, h);
    }
    System.out.println("Generacion 0");
    printMatrz(mapa);

    while (gConteo < g) {

      System.out.println("\nGeneracion: " + (gConteo + 1));

      nacerArbol(mapa);
      arbolSobrevivir(mapa);

      nacerAnimal(mapa, gConteo);
      animalSobrevivir(mapa);

      nacerAgua(mapa, gConteo);

      moverAnimales(mapa, n);

      printMatrz(mapa);

      gConteo++;

      if (s > 0) {
        try {
          Thread.sleep(s);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void nacerArbol(int[][] matriz) {
    int totalFilas = matriz.length;
    int totalColumnas = matriz[0].length;

    for (int fila = 0; fila < totalFilas; fila++) {
      for (int columna = 0; columna < totalColumnas; columna++) {

        if (matriz[fila][columna] == 0) {
          int arboles = 0;

          for (int filaVecino = fila - 1; filaVecino <= fila + 1; filaVecino++) {
            for (int columnaVecino = columna - 1; columnaVecino <= columna + 1; columnaVecino++) {

              if (filaVecino == fila && columnaVecino == columna) continue; // celda actual
              if (filaVecino >= 0 && filaVecino < totalFilas && columnaVecino >= 0 && columnaVecino < totalColumnas) {

                if (matriz[filaVecino][columnaVecino] == 1) {
                  arboles++;
                  if (arboles >= 2) break;
                }
              }
            }
            if (arboles >= 2) break;
          }
          if (arboles >= 2) {
            matriz[fila][columna] = 1;
            return;
          }
        }
      }
    }
  }

  public static void arbolSobrevivir(int[][] matriz) {
    int totalFilas = matriz.length;
    int totalColumnas = matriz[0].length;

    for (int fila = 0; fila < totalFilas; fila++) {
      for (int columna = 0; columna < totalColumnas; columna++) {

        if (matriz[fila][columna] == 1) { // celda vacía
          boolean tieneAgua = false;

          for (int filaVecino = fila - 2; filaVecino <= fila + 2; filaVecino++) {
            for (int columnaVecino = columna - 2; columnaVecino <= columna + 2; columnaVecino++) {

              if (filaVecino < 0 || filaVecino >= totalFilas || columnaVecino < 0 || columnaVecino >= totalColumnas)
                continue;
              if (filaVecino == fila && columnaVecino == columna) continue; // celda central

              if (matriz[filaVecino][columnaVecino] == 3) {
                tieneAgua = true;
              }
            }
          }

          if (!tieneAgua) {
            matriz[fila][columna] = 0;
          }
        }
      }
    }
  }

  public static void nacerAnimal(int[][] matriz, int gen) {
    int totalFilas = matriz.length;
    int totalColumnas = matriz[0].length;

    if (gen % 2 == 0) return;

    for (int fila = 0; fila < totalFilas; fila++) {
      for (int columna = 0; columna < totalColumnas; columna++) {

        if (matriz[fila][columna] == 0) {
          int animales = 0;
          int arboles = 0;
          int agua = 0;

          for (int filaVecino = fila - 2; filaVecino <= fila + 2; filaVecino++) {
            for (int columnaVecino = columna - 2; columnaVecino <= columna + 2; columnaVecino++) {

              if (filaVecino < 0 || filaVecino >= totalFilas || columnaVecino < 0 || columnaVecino >= totalColumnas) continue;

              if (filaVecino == fila && columnaVecino == columna) continue;
              int valor = matriz[filaVecino][columnaVecino];

              if (filaVecino >= fila - 1 && filaVecino <= fila + 1 && columnaVecino >= columna - 1 && columnaVecino <= columna + 1) {
                if (valor == 2) animales++;
              }

              if (valor == 1) arboles = 1;
              if (valor == 3) agua = 1;
            }
          }

          if (animales >= 2 && arboles >= 1 && agua >= 1) {
            matriz[fila][columna] = 2;
            return;
          }
        }
      }
    }
  }

  public static void animalSobrevivir(int[][] matriz) {
    int totalFilas = matriz.length;
    int totalColumnas = matriz[0].length;

    for (int fila = 0; fila < totalFilas; fila++) {
      for (int columna = 0; columna < totalColumnas; columna++) {

        if (matriz[fila][columna] == 2) {
          boolean tieneAgua = false;
          boolean tieneArbol = false;

          for (int filaVecino = fila - 2; filaVecino <= fila + 2; filaVecino++) {
            for (int columnaVecino = columna - 2; columnaVecino <= columna + 2; columnaVecino++) {

              if (filaVecino < 0 || filaVecino >= totalFilas || columnaVecino < 0 || columnaVecino >= totalColumnas)
                continue;
              if (filaVecino == fila && columnaVecino == columna) continue; // celda central

              int valor = matriz[filaVecino][columnaVecino];

              if (valor == 3) tieneAgua = true;
              if (valor == 1) tieneArbol = true;
            }
          }

          if (!tieneAgua || !tieneArbol) {
            matriz[fila][columna] = 0;
            return;
          }
        }
      }
    }
  }

  public static void nacerAgua(int[][] matriz, int gen) {
    int filas = matriz.length;
    int columnas = matriz[0].length;
    int agua = 0;

    if (gen % 3 != 0) return;

    for (int fila = 0; fila < filas; fila++) {
      for (int columna = 0; columna < columnas; columna++) {
        if (matriz[fila][columna] == 0){
          if (fila > 0 && matriz[fila - 1][columna] == 3 && matriz[fila][columna] != 3) {
            matriz[fila][columna] = 3;
            return;
          }
        }
      }
    }
  }

  public static void printMatrz(int[][] nMatriz){
    for (int fila = 0; fila < nMatriz.length; fila++){
      for (int columna = 0; columna<nMatriz[fila].length; columna++ ) {
        if (nMatriz[fila][columna] == 0) {
          System.out.print("[]" + "\t");
        } else if (nMatriz[fila][columna] == 1) {
          System.out.print("🌲" + "\t");
        } else if (nMatriz[fila][columna] == 2) {
          System.out.print("🦊" + "\t");
        } else if (nMatriz[fila][columna] == 3) {
          System.out.print("💦" + "\t");
        }
      }
      System.out.println();
    }
  }

  public static void moverAnimales(int[][] matriz, int n) {
    int filas = matriz.length;
    int columnas = matriz[0].length;

    int[][] nuevaMatriz = new int[filas][columnas];

    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < columnas; j++) {
        nuevaMatriz[i][j] = matriz[i][j];
      }
    }

    for (int fila = 0; fila < filas; fila++) {
      for (int col = 0; col < columnas; col++) {

        if (matriz[fila][col] == 2) { // Animal
          int nuevaFila = fila;
          int nuevaCol = col;

          switch (n) {
            case 1: nuevaCol = col + 1; break; // derecha
            case 2: nuevaFila = fila + 1; break; // abajo
            case 3: nuevaCol = col - 1; break; // izquierda
            case 4: nuevaFila = fila - 1; break; // arriba
          }

          if (nuevaFila >= 0 && nuevaFila < filas &&
                  nuevaCol >= 0 && nuevaCol < columnas) {

            int valorDestino = matriz[nuevaFila][nuevaCol];

            if (valorDestino == 0 || valorDestino == 1) {
              nuevaMatriz[nuevaFila][nuevaCol] = 2;
              nuevaMatriz[fila][col] = 0;
            }
          }
        }
      }
    }
    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < columnas; j++) {
        matriz[i][j] = nuevaMatriz[i][j];
      }
    }
  }

  public static int[][] generarMapaAleatorio(int w, int h) {
    int[][] mapa = new int[h][w];

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        mapa[i][j] = (int)(Math.random() * 4); // 0 a 3
      }
    }
    return mapa;
  }

  public static int[][] generarMapaDesdeTexto(String m, int w, int h) {

    int[][] mapa = new int[h][w];

    String[] filas = m.split("#");

    for (int fila = 0; fila < h; fila++) {
      if (fila < filas.length) {
        String filaTexto = filas[fila];
        for (int col = 0; col < w; col++) {
          if (col < filaTexto.length()) {
            mapa[fila][col] = Character.getNumericValue(filaTexto.charAt(col));
          } else {
            mapa[fila][col] = 0;
          }
        }
      } else {
        for (int col = 0; col < w; col++) {
          mapa[fila][col] = 0;
        }
      }
    }
    return mapa;
  }
}