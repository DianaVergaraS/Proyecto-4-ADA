package generador;

import java.io.FileWriter; //Para escribir archivos
import java.io.PrintWriter; //Para imprimir archivos
import java.util.ArrayList;
import java.util.HashMap;  //Para las claves únicas
import java.util.Stack;
import java.util.Collections;
import java.util.PriorityQueue;


public class grafo {

    private HashMap<Integer, arista> Aristas;
    private HashMap<Integer, vertice> Vertices;


    public grafo(HashMap<Integer, vertice> N, HashMap<Integer, arista> A) {
        this.Aristas = new HashMap();
        for (int i = 0; i < A.size(); i++) {
            this.Aristas.put(i, new arista(A.get(i)));
        }
        this.Vertices = new HashMap();
        for (int i = 0; i < N.size(); i++) {
            this.Vertices.put(i, new vertice(N.get(i)));
        }
    }

    public grafo() {
        this.Aristas = new HashMap();
        this.Vertices = new HashMap();
    }

    public grafo(grafo k) {
        this.Aristas = new HashMap();
        for (int i = 0; i < k.getAristas().size(); i++) {
            this.Aristas.put(i, new arista(k.getAristas().get(i)));
        }
        this.Vertices = new HashMap();
        for (int i = 0; i < k.getVertices().size(); i++) {
            this.Vertices.put(i, new vertice(k.getVertices().get(i)));
        }
    }

    public void setNodos(HashMap<Integer, vertice> w) {
        this.Vertices = w;
    }

    public void setAristas(HashMap<Integer, arista> w) {
        this.Aristas = w;
    }

    public HashMap<Integer, arista> getAristas() {
        return this.Aristas;
    }

    public HashMap<Integer, vertice> getVertices() {
        return this.Vertices;
    }

    // M�todo de Erdos-R�nyi
    public static grafo ErdosRenyi(int NumVertices, int NumAristas, int dirigido) {
        HashMap<Integer, vertice> VerticeS = new HashMap();
        HashMap<Integer, arista> AristaS = new HashMap();

        int AristasHechas;

        for (int i = 0; i < NumVertices; i++) {
            VerticeS.put(i, new vertice(i));
        }

        int x1 = (int) (Math.random() * NumVertices), x2 = (int) (Math.random() * NumVertices);
        AristaS.put(0, new arista(VerticeS.get(x1).get(), VerticeS.get(x2).get(), Math.random()));

        while (x1 == x2 && dirigido == 0) {
            x1 = (int) (Math.random() * NumVertices);
            x2 = (int) (Math.random() * NumVertices);
            AristaS.put(0, new arista(VerticeS.get(x1).get(), VerticeS.get(x2).get(), Math.random()));
        }

        VerticeS.get(x1).conectar();
        VerticeS.get(x2).conectar();
        if (x1 != x2) {
            VerticeS.get(x1).IncGrado(1);
        }
        VerticeS.get(x2).IncGrado(1);

        AristasHechas = 1;
        while (AristasHechas < NumAristas) {
            x1 = (int) (Math.random() * NumVertices);
            x2 = (int) (Math.random() * NumVertices);

            if (x1 != x2 || dirigido == 1) {
                int c1 = 1, cont = 0;
                while (c1 == 1 && cont < AristasHechas) {
                    int a = AristaS.get(cont).getan_from(), b = AristaS.get(cont).getan_to();
                    if ((x1 == a && x2 == b) || (x1 == b && x2 == a)) {
                        c1 = 0;
                    }
                    cont++;
                }
                if (c1 == 1) {
                    AristaS.put(AristasHechas, new arista(VerticeS.get(x1).get(), VerticeS.get(x2).get(), Math.random()));
                    VerticeS.get(x1).conectar();
                    VerticeS.get(x2).conectar();
                    if (x1 != x2) {
                        VerticeS.get(x1).IncGrado(1);
                    }
                    VerticeS.get(x2).IncGrado(1);
                    AristasHechas++;
                }
            }
        }

       
        grafo G = new grafo(VerticeS, AristaS);
        return G;

    }
    
 // M�todo de Gilbert
    public static grafo Gilbert(int NumVertices, double probabilidad, int dirigido) {
        HashMap<Integer, vertice> VerticeS = new HashMap();
        HashMap<Integer, arista> AristaS = new HashMap();

        int NumAristas = 0;

        for (int i = 0; i < NumVertices; i++) {
            VerticeS.put(i, new vertice(i));
        }

        for (int i = 0; i < NumVertices; i++) {
            for (int j = i; j < NumVertices; j++) {
                if (j != i || dirigido == 1) {
                    if (Math.random() <= probabilidad) {
                        AristaS.put(NumAristas, new arista(VerticeS.get(i).get(), VerticeS.get(j).get()));
                        VerticeS.get(i).conectar();
                        VerticeS.get(j).conectar();
                        if (i != j) {
                            VerticeS.get(i).IncGrado(1);
                        }
                        VerticeS.get(j).IncGrado(1);
                        NumAristas++;
                    }
                }
            }
        }
        grafo G = new grafo(VerticeS, AristaS);
        return G;
    }
    
    //M�todo Geogr�fico
    public static grafo Geografico(int NumVertices, double distancia, int dirigido) {
        HashMap<Integer, vertice> VerticeS = new HashMap();
        HashMap<Integer, arista> AristaS = new HashMap();
        int NumAristas = 0;

        for (int i = 0; i < NumVertices; i++) {
            VerticeS.put(i, new vertice(i, Math.random(), Math.random()));
        }

        for (int i = 0; i < NumVertices; i++) {
            for (int j = i; j < NumVertices; j++) {
                if (j != i || dirigido == 1) {
                    double dis = Math.sqrt(Math.pow(VerticeS.get(j).getX() - VerticeS.get(i).getX(), 2)
                            + Math.pow(VerticeS.get(j).getY() - VerticeS.get(i).getY(), 2));
                    if (dis <= distancia) {
                        AristaS.put(NumAristas, new arista(VerticeS.get(i).get(), VerticeS.get(j).get()));
                        VerticeS.get(i).IncGrado(1);
                        VerticeS.get(i).conectar();
                        if (j != i) {
                            VerticeS.get(j).IncGrado(1);
                            VerticeS.get(j).conectar();
                        }
                        NumAristas++;
                    }
                }
            }
        }
        grafo G = new grafo(VerticeS, AristaS);
        return G;
    }

// M�todo Bar�basi
    public static grafo Barabasi(int NumVertices, double d, int dirigido) {
        HashMap<Integer, vertice> VerticeS = new HashMap();
        HashMap<Integer, arista> AristaS = new HashMap();

        int NumAristas = 0;

        for (int i = 0; i < NumVertices; i++) {
            VerticeS.put(i, new vertice(i));
        }

        for (int i = 0; i < NumVertices; i++) {
            int j = 0;
            while (j <= i && VerticeS.get(i).getGrado() <= d) {
                if (j != i || dirigido == 1) {
                    if (Math.random() <= 1 - VerticeS.get(j).getGrado() / d) {
                        AristaS.put(NumAristas, new arista(VerticeS.get(i).get(), VerticeS.get(j).get()));
                        VerticeS.get(i).IncGrado(1);
                        VerticeS.get(i).conectar();
                        if (j != i) {
                            VerticeS.get(j).IncGrado(1);
                            VerticeS.get(j).conectar();
                        }
                        NumAristas++;
                    }
                }
                j++;
            }
        }
        grafo G = new grafo(VerticeS, AristaS);
        return G;
    }

    public static void construir(String nombre, grafo g) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        //System.out.println(g.getVertices().size());
        try {
            fichero = new FileWriter(nombre + ".gv");
            pw = new PrintWriter(fichero);
            pw.println("graph {"); //Para Gephi
            pw.println();
            for (int i = 0; i < g.getAristas().size(); i++) {
                pw.println(g.getAristas().get(i).getan_from() + "--" + g.getAristas().get(i).getan_to() + "  " + " ");
            }
            pw.println("}"); // print v�rtices y aristas

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public void setG(HashMap<Integer, vertice> a, HashMap<Integer, arista> b) {
        this.Vertices = (HashMap) a;
        this.Aristas = (HashMap) b;
    }
    
    public static grafo BFS(grafo G1, vertice nod) {
        grafo G = new grafo(G1.getVertices(), G1.getAristas());
        HashMap<Integer, HashMap> Ls = new HashMap();
        HashMap<Integer, vertice> Ln1 = new HashMap();
        HashMap<Integer, vertice> Ln2 = new HashMap();
        HashMap<Integer, vertice> V = new HashMap();
        HashMap<Integer, arista> Edg = new HashMap();
        int numL = 0, cv = 0, num = 0;

        G.getVertices().get(nod.get()).setfal(true);
        Ln1.put(0, G.getVertices().get(nod.get()));
        Ls.put(numL, (HashMap) Ln1.clone());
        V.put(cv, G.getVertices().get(nod.get()));

        while (Ln1.isEmpty() == false) {
            Ln2.clear();
            num = 0;
            for (int i = 0; i < Ln1.size(); i++) {
                for (int j = 0; j < G.getAristas().size(); j++) {
                    if (Ln1.get(i).get() == G.getAristas().get(j).getan_from() && G.getVertices().get(G.getAristas().get(j).getan_to()).getfal() == false) {
                        G.getVertices().get(G.getAristas().get(j).getan_to()).setfal(true);
                        Ln2.put(num, G.getVertices().get(G.getAristas().get(j).getan_to()));
                        num++;
                        Edg.put(cv, G.getAristas().get(j));
                        cv++;
                        V.put(cv, G.getVertices().get(G.getAristas().get(j).getan_to()));
                    }
                    if (Ln1.get(i).get() == G.getAristas().get(j).getan_to() && G.getVertices().get(G.getAristas().get(j).getan_from()).getfal() == false) {
                        G.getVertices().get(G.getAristas().get(j).getan_from()).setfal(true);
                        Ln2.put(num, G.getVertices().get(G.getAristas().get(j).getan_from()));
                        num++;
                        Edg.put(cv, G.getAristas().get(j));
                        cv++;
                        V.put(cv, G.getVertices().get(G.getAristas().get(j).getan_from()));
                    }
                }
            }
            numL++;
            Ln1 = (HashMap) Ln2.clone();
            Ls.put(numL, (HashMap) Ln2.clone());
        }
        grafo A = new grafo();
        A.setG(V, Edg);
        return A;
    }
    
    public static grafo DFS_I(grafo G1, vertice nod) {
        grafo G = new grafo(G1.getVertices(), G1.getAristas());
        grafo A = new grafo();
        int z, cA = 0;
        boolean fl;

        boolean MA[][] = new boolean[G.getVertices().size()][G.getVertices().size()];
        
        for (int i = 0; i < G.getAristas().size(); i++) {
            if (G.getAristas().get(i).getfal() == false) {
                MA[G.getAristas().get(i).getan_from()][G.getAristas().get(i).getan_to()]=true;
                MA[G.getAristas().get(i).getan_to()][G.getAristas().get(i).getan_from()]=true;
            }
        }

        Stack<Integer> pila = new Stack<>();
        pila.push(G.getVertices().get(nod.get()).get());
        G.getVertices().get(nod.get()).setfal(true);
        A.getVertices().put(cA, new vertice(G.getVertices().get(nod.get())));

        while (pila.isEmpty() == false) {
            z = pila.peek();
            fl = false;
            for (int i = 0; i < G.getVertices().size(); i++) {
                if (MA[z][i] == true && G.getVertices().get(i).getfal() == false) {
                    G.getVertices().get(i).setfal(true);
                    A.getAristas().put(cA, new arista(z, i));
                    cA++;
                    A.getVertices().put(cA, new vertice(G.getVertices().get(i)));
                    pila.push(i);
                    fl = true;
                    i = G.getVertices().size();
                }
                if (i == G.getVertices().size() - 1 && fl == false) {
                    pila.pop();
                }
            }
        }

    
        return A;
    }

    public static grafo DFS_R(grafo G, vertice N0) {
        grafo A = new grafo();
        grafo B;
        boolean MA[][] = new boolean[G.getVertices().size()][G.getVertices().size()];
        for (int i = 0; i < G.getAristas().size(); i++) {
            MA[G.getAristas().get(i).getan_from()][G.getAristas().get(i).getan_to()] = true;
            MA[G.getAristas().get(i).getan_to()][G.getAristas().get(i).getan_from()] = true;
        }
        G.getVertices().get(N0.get()).setfal(true);
        A.getVertices().put(0, new vertice(G.getVertices().get(N0.get())));
        for (int i = 0; i < G.getVertices().size(); i++) {
            if (MA[N0.get()][i] == true && G.getVertices().get(i).getfal() == false) {
                B = DFS_R(G, G.getVertices().get(i));
                int tN = A.getVertices().size();
                for (int j = 0; j < B.getVertices().size(); j++) {
                    A.getVertices().put(tN + j, B.getVertices().get(j));
                }
                A.getAristas().put(A.getAristas().size(), new arista(N0.get(), i));
                tN = A.getAristas().size();
                if (B.getAristas().isEmpty() != true) {
                    for (int j = 0; j < B.getAristas().size(); j++) {
                        A.getAristas().put(tN + j, B.getAristas().get(j));
                    }
                }
            }
        }
        return A;
    }
    
    public grafo Dijkstra(vertice fuente) {
        for (int i = 0; i < this.Vertices.size(); i++) {
            this.Vertices.get(i).setwi(Double.POSITIVE_INFINITY); 
        }
        double COSTOS[][] = new double[this.Vertices.size()][this.Vertices.size()];

        for (int i = 0; i < this.Vertices.size(); i++) {
            for (int k = 0; k < this.Vertices.size(); k++) {
               COSTOS[i][k] = Double.POSITIVE_INFINITY;
            }
        }
        for (int k = 0; k < this.Aristas.size(); k++) {
            COSTOS[this.Aristas.get(k).getan_from()][this.Aristas.get(k).getan_to()] = this.Aristas.get(k).getArbol();
            COSTOS[this.Aristas.get(k).getan_to()][this.Aristas.get(k).getan_from()] = this.Aristas.get(k).getArbol();
        }
        ArrayList<Integer> Conjunto_vertices = new ArrayList<Integer>();
        HashMap<Integer, arista> b_n = new HashMap(); 
        int arista_actual = 0;
        Conjunto_vertices.add(this.Vertices.get(fuente.get()).get());
        this.Vertices.get(fuente.get()).setfal(true);
        this.Vertices.get(fuente.get()).setwi(0);
        
        double distancia = 0; 
        int x = 0, y = 0;
        boolean bandera = true;
       
        while (distancia != Double.POSITIVE_INFINITY) {
            distancia = Double.POSITIVE_INFINITY;
            
            for (int i = 0; i < Conjunto_vertices.size(); i++) {
                for (int p = 0; p < this.Vertices.size(); p++) {
                    if (this.Vertices.get(p).getfal() != true && COSTOS[Conjunto_vertices.get(i)][p] != Double.POSITIVE_INFINITY) {
                        if ((this.Vertices.get(Conjunto_vertices.get(i)).getwin() + COSTOS[Conjunto_vertices.get(i)][p]) < distancia) {
                            this.Vertices.get(p).setwi(this.Vertices.get(Conjunto_vertices.get(i)).getwin() + COSTOS[Conjunto_vertices.get(i)][p]);
                            distancia = this.Vertices.get(p).getwin();
                            x = Conjunto_vertices.get(i);
                            y = p;
                        }
                    }
                }
            }
            if (distancia != Double.POSITIVE_INFINITY) {
                this.Vertices.get(y).setfal(true);
                Conjunto_vertices.add(y);
                System.out.println("          " + distancia + "   " + x +"-->"+ y);
                b_n.put(arista_actual, new arista(x, y, COSTOS[x][y]));
                arista_actual++;
                }

        }

        HashMap<Integer, vertice> a_n = new HashMap();
        for (int i = 0; i < Conjunto_vertices.size(); i++) {
            a_n.put(i, new vertice(this.Vertices.get(Conjunto_vertices.get(i))));
        }
        grafo G1 = new grafo(a_n, b_n);
        return G1;
        

    }
    
    public grafo RandomEdgeValues(double min, double max) {
        int num_a = this.Aristas.size();
        for (int j = 0; j < num_a; j++) {
            this.Aristas.get(j).setP(Math.random() * (max - min) + min);
        }
        grafo G_1 = new grafo(this.Vertices, this.Aristas);
        return G_1;
    }
    
    public grafo Prim() {
        
        grafo G = new grafo(this.Vertices, this.Aristas);
        
        arista COSTOS[][] = new arista[G.getVertices().size()][G.getVertices().size()]; 
        
        for (int i = 0; i < G.getAristas().size(); i++) {
            COSTOS[G.getAristas().get(i).getan_from()][G.getAristas().get(i).getan_to()] = G.getAristas().get(i);
            COSTOS[G.getAristas().get(i).getan_to()][G.getAristas().get(i).getan_from()] = G.getAristas().get(i);
        }
        ArrayList<Integer> Conjunto_vertices = new ArrayList<Integer>(); 
        HashMap<Integer, arista> P_aristas = new HashMap(); 
        
        int n = 0;
        Conjunto_vertices.add(G.getVertices().get(0).get());
        G.getVertices().get(0).setfal(true);
        PriorityQueue<arista> prior_q = new PriorityQueue<>();
        double arbol_prim = 0;
        for (int i = 0; i < G.getAristas().size(); i++) {
            for (int k = 0; k < Conjunto_vertices.size(); k++) {
                for (int j = 0; j < G.getVertices().size(); j++) {
                    if (COSTOS[Conjunto_vertices.get(k)][j] != null && G.getVertices().get(j).getfal() == false) {
                        prior_q.add(COSTOS[Conjunto_vertices.get(k)][j]);
                    }
                }
            }
            if (prior_q.isEmpty() == false) {
                if (Conjunto_vertices.contains(prior_q.peek().getan_from())) {
                    G.getVertices().get(prior_q.peek().getan_to()).setfal(true);
                    Conjunto_vertices.add(prior_q.peek().getan_to());
                    P_aristas.put(n, COSTOS[prior_q.peek().getan_from()][prior_q.peek().getan_to()]);
                    n++;
                    arbol_prim = arbol_prim + prior_q.peek().getArbol();
                    prior_q.clear();
                } else {
                    G.getVertices().get(prior_q.peek().getan_from()).setfal(true);
                    Conjunto_vertices.add(prior_q.peek().getan_from());
                    P_aristas.put(n, COSTOS[prior_q.peek().getan_from()][prior_q.peek().getan_to()]);
                    n++;
                    arbol_prim = arbol_prim + prior_q.peek().getArbol();
                    prior_q.clear();
                }
            }
            if (P_aristas.size() == G.getVertices().size() - 1) {
                break;
            }
        }
        System.out.println("Peso total Prim = "+arbol_prim);
        grafo ARBOL = new grafo(G.getVertices(), P_aristas);
        return ARBOL;
    }
    
    public grafo Kruskal_D() {
        
        grafo G = new grafo(this.Vertices, this.Aristas);
        
        PriorityQueue<arista> prior_q = new PriorityQueue<>();
        for (int k = 0; k < G.getAristas().size(); k++) {
            prior_q.add(G.getAristas().get(k));
        }
        HashMap<Integer, arista> arbol_min = new HashMap(); 
        HashMap<Integer, vertice> arbol_kruskal = new HashMap();
        ArrayList<Integer> Conjunto_vertices = new ArrayList<Integer>(); 
        
        int z = 0;
        int y = 0;
        double arbol_k = 0;
        for (int j = 0; j < G.getAristas().size(); j++) {
            if (Conjunto_vertices.contains(prior_q.peek().getan_from()) && Conjunto_vertices.contains(prior_q.peek().getan_to())) {
                grafo a_1 = new grafo(G.getVertices(), arbol_min); 
                grafo a_2 = new grafo();
                a_2 = DFS_I(a_1, a_1.getVertices().get(prior_q.peek().getan_from()));
                int f = 0;
                for (int i = 0; i < a_2.getVertices().size(); i++) {
                    if (a_2.getVertices().get(i).get() == prior_q.peek().getan_to()) {
                        f = 1;
                    }
                }
                if (f == 0) {
                    arbol_k = arbol_k + prior_q.peek().getArbol();
                    arbol_min.put(z, prior_q.poll());
                    z++;
                } else {
                    prior_q.poll();
                }
            } else {
                if (Conjunto_vertices.contains(prior_q.peek().getan_from()) == false) {
                    Conjunto_vertices.add(prior_q.peek().getan_from());
                }
                if (Conjunto_vertices.contains(prior_q.peek().getan_to()) == false) {
                    Conjunto_vertices.add(prior_q.peek().getan_to());
                }
                arbol_k = arbol_k + prior_q.peek().getArbol();
                arbol_min.put(z, prior_q.poll());
                z++;
            }
            if (arbol_min.size() == G.getVertices().size() - 1) {
                j = G.getAristas().size();
            }
        }
        for (int i = 0; i < Conjunto_vertices.size(); i++) {
            arbol_kruskal.put(i, G.getVertices().get(Conjunto_vertices.get(i)));
        }
        System.out.println("Peso total Kruskal-Directo = "+arbol_k);
        grafo ARBOL = new grafo(arbol_kruskal, arbol_min);
        return ARBOL;
    }
    
    public grafo Kruskal_I() {
        
        grafo G = new grafo(this.Vertices, this.Aristas);
        
        for (int k = 0; k < G.getAristas().size(); k++) 
            G.getAristas().get(k).setID(k);
        PriorityQueue<arista> prior_q = new PriorityQueue<>(Collections.reverseOrder());
        
        for (int m = 0; m < G.getAristas().size(); m++) 
            prior_q.add(G.getAristas().get(m));
        int z = 0; 
        HashMap<Integer, arista> arbol_min = new HashMap();
        grafo aux = new grafo();
        arista quitar = new arista();
        double a_min = 0;
        for (int i = 0; i < G.getAristas().size(); i++) {
            quitar = prior_q.poll();
            G.getAristas().get(quitar.getID()).setfal(true);
            aux = DFS_I(G, G.getVertices().get(quitar.getan_from()));
            if (aux.getVertices().size()<G.getVertices().size()){
                arbol_min.put(z, new arista(quitar.getan_from(), quitar.getan_to(), quitar.getArbol()));
                z++;
                G.getAristas().get(quitar.getID()).setfal(false);
                a_min = a_min + quitar.getArbol();
            } 
        }
        System.out.println("Peso total Kruskal-Inverso = "+a_min);
        
        grafo ARBOL = new grafo(G.getVertices(), arbol_min);
        return ARBOL;
    }
    
    public static void main(String[] args) {
    	//Primera Entrega
        grafo grafo_creado = new grafo();
        //grafo_creado = ErdosRenyi(500,500,0);
        //grafo_creado = Gilbert(500,.19,0);
        //grafo_creado= Geografico(500,.4,0);        
        grafo_creado = Barabasi(500,500,0);
        construir("Bar3",grafo_creado);
        
        //Segunda Entrega
        //grafo grafoBFS = new grafo(BFS(grafo_creado,grafo_creado.getVertices().get(0)));
        //construir("grafoBFS",grafoBFS);
        //grafo grafoDFSI = new grafo(DFS_I(grafo_creado,grafo_creado.getVertices().get(0)));
        //construir("grafoDFS-I",grafoDFSI);
        //grafo grafoDFSR = new grafo(DFS_R(grafo_creado,grafo_creado.getVertices().get(0)));
        //construir("grafoDFS-R",grafoDFSR);
        
        //Tercera entrega
        //grafo_creado.RandomEdgeValues(5,30);
        //grafo_creado = grafo_creado.Dijkstra(grafo_creado.getVertices().get(1)); // El nodo 1 es el nodo inicio o raíz
        //construir("Dijkstra_Bar3",grafo_creado);
        
        //Cuarta entrega
        grafo_creado.RandomEdgeValues(1,50); //Valores entre los cuales estarán los pesos
        
        grafo Grafo_Prim = new grafo();
        Grafo_Prim = grafo_creado.Prim();
        construir("Prim_Bar3", Grafo_Prim);
        
        grafo Grafo_Kruskal_Directo = new grafo();
        Grafo_Kruskal_Directo = grafo_creado.Kruskal_D();
        construir("Kruskal-D_Bar3", Grafo_Kruskal_Directo);
        
        grafo Grafo_Kruskal_Inverso = new grafo();
        Grafo_Kruskal_Inverso = grafo_creado.Kruskal_I();
        construir("Kruskal-I_Bar3", Grafo_Kruskal_Inverso);
    }
}
