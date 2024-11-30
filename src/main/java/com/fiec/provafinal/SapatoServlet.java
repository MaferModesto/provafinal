package com.fiec.provafinal;

import com.fiec.provafinal.models.Sapato;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/sapatos")
public class SapatoServlet extends HttpServlet {

    private EntityManager em;

    public SapatoServlet() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("minhaUnidadeDePersistencia");
        this.em = entityManagerFactory.createEntityManager();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Sapato> produtoList = em.createQuery("select t from " + Sapato.class.getSimpleName() + " t").getResultList();
        System.out.println(produtoList);
        resp.setContentType("text/html");
        String conteudo = "";
        if (produtoList != null) {
            conteudo = produtoList.stream().map(p -> p.toString()).collect(Collectors.joining("<br/>"));
        }
        resp.getWriter().println(conteudo);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");
        String nome = req.getParameter("nome");
        String tamanhoStr = req.getParameter("tamanho");
        String imagem = req.getParameter("imagem");
        String marca = req.getParameter("marca");
        String precoStr = req.getParameter("preco");

        double preco = Double.parseDouble(precoStr);


        int tamanho = 0;
        if (tamanhoStr != null && !tamanhoStr.isEmpty()) {
            try {
                tamanho = Integer.parseInt(tamanhoStr);
            } catch (NumberFormatException e) {
            }
                return;
            }
        Sapato p = Sapato.builder()
                .id(id)
                .nome(nome)
                .imagem(imagem)
                .preco(preco)
                .tamanho(tamanho)
                .marca(marca)

                .build();
        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Hello from POST do SAPATO!</h1>");
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = getId(request);


        response.setContentType("text/html");
        response.getWriter().println("Sapato Atualizado");

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = getId(request);

        response.setContentType("text/html");
        response.getWriter().println("Sapato Deletado");
    }

    private static String getId(HttpServletRequest req) {
        String path = req.getPathInfo();
        String[] paths = path.split("/");
        String id = paths[paths.length - 1];
        return id;
    }
}