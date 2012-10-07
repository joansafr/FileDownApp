package controlador;

import java.net.*;
import java.io.*;
import vista.Principal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class webController {

    Integer contador = 1;
    Principal instanciaPrincipal = null;

    public webController() {
    }

    public void descarga(String ruta, String mime, boolean es_archivo) throws IOException {
        //instanciaPrincipal = instPrincipal;
        if (es_archivo) {
            obtenerArchivo(ruta, mime);
        } else {
            obtenerPagina(ruta, mime);
        }
        contador = 1;
    }

    private void obtenerPagina(String ruta, String mime) throws IOException {
        URL oracle;
        BufferedReader in = null;


        try {
            oracle = new URL(ruta);
            in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                pullLinks(inputLine,mime);
            }
            in.close();
        } catch (Exception exObtenerPagina) {
            JOptionPane.showMessageDialog(null, exObtenerPagina);

        } finally {
            in.close();
            //instanciaPrincipal.setTEXTLABEL("Los archivos generados no cuentan con extension.");
        }
    }

    private void obtenerArchivo(String ruta, String mime) throws IOException {
        FileReader archivo = null;
        BufferedReader buff = null;
        try {
            archivo = new FileReader(ruta);
            buff = new BufferedReader(archivo);
            String inputLine = "";
            while ((inputLine = buff.readLine()) != null) {
                pullLinks(inputLine,mime);
            }
        } catch (Exception exObtenerPagina) {
            JOptionPane.showMessageDialog(null, exObtenerPagina);

        } finally {
            buff.close();
             //instanciaPrincipal.setTEXTLABEL("Los archivos generados no cuentan con extension.");
        }
    }

    private String obtenerNombreDeArchivo(String url) {
        int slashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.', slashIndex);
        String filenameWithoutExtension;
        if (dotIndex == -1) {
            filenameWithoutExtension = url.substring(slashIndex + 1);
        } else {
            filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
        }
        return filenameWithoutExtension;
    }

    private void pullLinks(String text, String mime) throws IOException {
        ArrayList links = new ArrayList();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            descargarArchivos(urlStr, mime);
        }
        //return links;
    }

    private void descargarArchivos(String urlpass, String mime) throws IOException {
        URL url1 = new URL(urlpass);

        byte[] ba1 = new byte[1024];
        int baLength;
        FileOutputStream fos1 = null;

        try {
            // Contacting the URL

            URLConnection urlConn = url1.openConnection();

            String hola = urlConn.getContentType();
            //instanciaPrincipal.setTEXTLABEL("Conectando a " + url1.toString() + " ... \n");
            System.out.println("Conectando a " + url1.toString() + " ... \n");

            if (!(hola.equalsIgnoreCase(mime))) {
                return;
            }

            try {
                fos1 = new FileOutputStream("Archivo" + contador.toString());

                InputStream is1 = url1.openStream();
                while ((baLength = is1.read(ba1)) != -1) {
                    fos1.write(ba1, 0, baLength);
                }

                fos1.flush();
                fos1.close();
                is1.close();
                //instanciaPrincipal.setTEXTLABEL("El archivo es: Archivo" + contador.toString());
                System.out.println("El archivo es: Archivo" + contador.toString());
                contador++;

            } catch (ConnectException ce) {
                //instanciaPrincipal.setTEXTLABEL("FAILED.[" + ce.getMessage() + "]\n");
                return;
            }


        } catch (NullPointerException npe) {
            //instanciaPrincipal.setTEXTLABEL("FAILED.[" + npe.getMessage() + "]\n");
            return;
        }


    }
}
