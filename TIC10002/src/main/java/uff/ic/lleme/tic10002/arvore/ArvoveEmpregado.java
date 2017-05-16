package uff.ic.lleme.tic10002.arvore;

import uff.ic.lleme.tic10002.ColecaoEmpregado;
import uff.ic.lleme.tic10002.Empregado;

public class ArvoveEmpregado implements ColecaoEmpregado {

    private No raiz = null;
    private int quantidadeNos = 0;

    private class No {

        private Empregado conteudo;
        private No pai = null;
        private No esquerda = null;
        private No direita = null;

        public No(Empregado empregado) {
            this.conteudo = empregado;
        }

        private boolean ehEsquerda() {
            if (pai != null)
                return pai.esquerda == this;
            else
                return false;
        }

        private boolean ehDireita() {
            if (pai != null)
                return pai.direita == this;
            else
                return false;
        }

        private boolean ehFolha() {
            return esquerda == null && direita == null;
        }

        private boolean temFilhoUnico() {
            return !(esquerda != null && direita != null);
        }

        private No filhoUnico() {
            if (esquerda == null && direita != null)
                return direita;
            else if (esquerda != null && direita == null)
                return esquerda;
            else
                return null;
        }

        private No filhoFolha() {
            if (esquerda != null && esquerda.ehFolha())
                return esquerda;
            else if (direita != null && direita.ehFolha())
                return direita;
            else
                return null;
        }

        private boolean temFolha() {
            return esquerda != null && esquerda.ehFolha() || direita != null && direita.ehFolha();
        }

        private No connectarADireita(No filho) {
            if (pai != null) {
                pai.direita = filho;
                if (filho != null)
                    filho.pai = pai;
            }
            return filho;
        }

        private No connectarAEsquerda(No filho) {
            if (pai != null) {
                pai.esquerda = filho;
                if (filho != null)
                    filho.pai = pai;
            }
            return filho;
        }

        private No irmao() {
            if (pai != null)
                if (pai.direita == this)
                    return pai.esquerda;
                else
                    return pai.direita;
            else
                return null;
        }

        private No conectar(No filho) {
            if (filho != null && filho.conteudo != null && conteudo.compararInstancia(filho.conteudo) < 0) {
                direita = filho;
                filho.pai = this;
            } else if (filho != null && filho.conteudo != null && conteudo.compararInstancia(filho.conteudo) > 0) {
                direita = filho;
                filho.pai = this;
            }
            return filho;
        }
    }

    private static boolean ehValido(Empregado empregado) {
        return empregado != null && empregado.getChave() != null;
    }

    private boolean ehValido(No no, String cpf) {
        return no != null && cpf != null;
    }

    @Override
    public Empregado buscar(String cpf) {
        return buscar(raiz, cpf);
    }

    private Empregado buscar(No no, String cpf) {
        if (no != null)
            if (no.conteudo.compararChave(cpf) == 0)
                return no.conteudo;
            else if (no.conteudo.compararChave(cpf) < 0)
                return buscar(no.esquerda, cpf);
            else
                return buscar(no.direita, cpf);
        else
            return null;

    }

    @Override
    public Empregado incluir(Empregado empregado) {
        if (ehValido(empregado))
            if (raiz == null) {
                quantidadeNos = 1;
                return (raiz = new No(empregado)).conteudo;
            } else
                return incluir(raiz, empregado);
        return null;
    }

    private Empregado incluir(No no, Empregado empregado) {
        if (no.conteudo.compararInstancia(empregado) == 0)
            return null;
        else if (no.conteudo.compararInstancia(empregado) < 0)
            if (no.esquerda == null) {
                quantidadeNos++;
                return (no.connectarAEsquerda(new No(empregado))).conteudo;
            } else
                return incluir(no.esquerda, empregado);
        else if (no.conteudo.compararInstancia(empregado) > 0)
            if (no.direita == null) {
                quantidadeNos++;
                return (no.connectarADireita(new No(empregado))).conteudo;
            } else
                return incluir(no.direita, empregado);
        else
            return null;
    }

    @Override
    public Empregado excluir(String cpf) {
        if (ehValido(raiz, cpf))
            return excluir(raiz, cpf);
        else
            return null;
    }

    private Empregado excluir(No no, String cpf) {
        No excluido = null;
        if (no.conteudo.compararChave(cpf) == 0) {
            excluido = no;
            if (no.ehFolha())
                if (no.ehDireita())
                    no.connectarADireita(null);
                else
                    no.connectarAEsquerda(null);
            else if (no.temFilhoUnico())
                if (no.ehDireita())
                    no.connectarADireita(no.filhoUnico());
                else
                    no.connectarAEsquerda(no.filhoUnico());
            else if (no.temFolha())
                if (no.ehDireita()) {
                    No filhoFolha = no.filhoFolha();
                    No irmaoFilhoFolha = filhoFolha.irmao();
                    no.connectarADireita(filhoFolha);
                    filhoFolha.conectar(irmaoFilhoFolha);
                } else {
                    No filhoFolha = no.filhoFolha();
                    No irmaoFilhoFolha = filhoFolha.irmao();
                    no.connectarAEsquerda(filhoFolha);
                    filhoFolha.conectar(irmaoFilhoFolha);
                }
            else if (no.ehDireita())
                no.pai.direita = null; //complatar
            else
                no.pai.esquerda = null; // completar
            return excluido.conteudo;
        } else if (no.conteudo.compararChave(cpf) < 0)
            return excluir(no.direita, cpf);
        else if (no.conteudo.compararChave(cpf) > 0)
            return excluir(no.esquerda, cpf);
        else
            return null;
    }

}
