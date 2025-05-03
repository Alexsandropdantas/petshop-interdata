package com.petshop.comum;

public class BibliotecaDeMetodosComunsAoSistema {

    // Método comum para devolver adequadamente o caminho das imagens na web, salvar no banco de dados e recuperar nas páginas Web
    public static String caminhoDasImagensWeb(String caminho){
        return caminho.replace("src/main/resources/static/", "").replace("src\\main\\resources\\static\\", "");
    }

}
