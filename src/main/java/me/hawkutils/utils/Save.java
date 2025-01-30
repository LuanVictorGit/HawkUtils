package me.hawkutils.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class Save implements Serializable {

    private static final long serialVersionUID = 8549640245219903487L;
    private List<Object> saves;
    private File file;

    // Construtor para Java 8 - Substituindo List.of por ArrayList
    public Save(File file, Object... saves) {
        this(file, convertToList(saves)); // Usando método auxiliar para conversão
    }

    // Construtor que recebe uma lista de objetos
    public Save(File file, List<Object> saves) {
        this.saves = new ArrayList<>(saves); // Evitar modificações externas na lista original
        this.file = file;
        writeToFile(); // Centralizar lógica de gravação
    }

    // Método para centralizar a lógica de gravação de dados no arquivo
    private void writeToFile() {
        Gson gson = new GsonBuilder().create();
        String save = gson.toJson(this.saves);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(save);
        } catch (IOException e) {
            e.printStackTrace(); // Melhorar tratamento de erro, talvez lançar exceção
        }
    }

    // Método estático para carregar dados do arquivo
    public static List<Object> load(File file) {
        try (FileReader reader = new FileReader(file);
             JsonReader jsonReader = new JsonReader(reader)) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(jsonReader, List.class);
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace(); // Logando o erro caso necessário
            return Collections.emptyList(); // Evitar criar uma nova instância desnecessária
        } catch (IOException e) {
            e.printStackTrace(); // Logando o erro
            return Collections.emptyList();
        }
    }

    // Método auxiliar para converter o varargs em uma lista
    private static List<Object> convertToList(Object[] saves) {
        List<Object> list = new ArrayList<>();
        for (Object save : saves) {
            list.add(save);
        }
        return list;
    }

    // Getters e Setters

    public List<Object> getSaves() {
        return saves;
    }

    public void setSaves(List<Object> saves) {
        this.saves = saves;
        writeToFile(); // Atualiza o arquivo sempre que a lista é alterada
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        writeToFile(); // Atualiza o arquivo se o caminho do arquivo for alterado
    }
}
