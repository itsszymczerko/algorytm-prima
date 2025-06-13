package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int V;

        while (true) {
            V = readInt(sc, "Podaj liczbe wierzcholkow (>0): ");
            if (V > 0) break;
            System.out.println("❌  Liczba wierzcholkow musi byc dodatnia.");
        }

        int E;
        while (true) {
            E = readInt(sc, "Podaj liczbe krawedzi (≥ " + (V-1) + "): ");
            if (E >= V - 1) break;
            System.out.println("❌  Graf spojny wymaga co najmniej " + (V-1) + " krawedzi.");
        }

        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < V; i++) graph.add(new ArrayList<>());

        System.out.println("\nPodaj krawedzie w formacie: <u> <v> <waga>");
        int added = 0;
        while (added < E) {
            int u = readInt(sc, "[" + (added+1) + "/" + E + "] u = ");
            int v = readInt(sc, "             v = ");
            int w = readInt(sc, "             w = ");

            if (u < 0 || u >= V || v < 0 || v >= V) {
                System.out.println("❌  Wierzcholki musza być w zakresie 0‒" + (V-1));
                continue;
            }
            if (u == v) {
                System.out.println("❌  Pomin petle (u ≠ v).");
                continue;
            }
            if (w <= 0) {
                System.out.println("❌  Waga musi być dodatnia.");
                continue;
            }

            addEdge(graph, u, v, w);
            added++;
        }

        int start;
        while (true) {
            start = readInt(sc, "\nPodaj wierzcholek startowy (0-" + (V-1) + "): ");
            if (start >= 0 && start < V) break;
            System.out.println("❌  Niepoprawny indeks.");
        }

        primMST(graph, start);
        sc.close();
    }

    public static class Edge {
        int target, weight;
        Edge(int target, int weight) {
            this.target = target;
            this.weight  = weight;
        }
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("❌  To nie jest liczba calkowita. Spróbuj ponownie.");
                sc.nextLine();
            }
        }
    }

    private static void addEdge(List<List<Edge>> g, int u, int v, int w) {
        g.get(u).add(new Edge(v, w));
        g.get(v).add(new Edge(u, w));
    }

    public static void primMST(List<List<Edge>> graph, int start) {
        int V = graph.size();
        boolean[] visited = new boolean[V];
        int[] key    = new int[V];
        int[] parent = new int[V];
        Arrays.fill(key,    Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        key[start] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, start});

        while (!pq.isEmpty()) {
            int u = pq.poll()[1];
            if (visited[u]) continue;
            visited[u] = true;

            for (Edge e : graph.get(u)) {
                int v = e.target, w = e.weight;
                if (!visited[v] && w < key[v]) {
                    key[v]    = w;
                    parent[v] = u;
                    pq.offer(new int[]{w, v});
                }
            }
        }

        System.out.println("\nKrawedzie MST:");
        int total = 0;
        for (int v = 0; v < V; v++) {
            if (parent[v] != -1) {
                System.out.printf("%d — %d  (waga: %d)%n", parent[v], v, key[v]);
                total += key[v];
            }
        }
        System.out.println("Calkowita waga MST: " + total);
    }
}
