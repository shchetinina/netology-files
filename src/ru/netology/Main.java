package ru.netology;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    private static final StringBuilder log = new StringBuilder();

    private static void makeDirectory(String path){
        var directory = new File(path);
        try {
            if(directory.mkdir()) {
                log.append(LocalDateTime.now().toString() + "\t"+ directory.getPath() + " was created.\n");
            }else {
                log.append(LocalDateTime.now().toString() + "\t" + directory.getPath() + " wasn't created.\n");
            }
        }
        catch (Exception ex) {
            log.append(LocalDateTime.now().toString() + "\t" + directory.getPath() + " wasn't created.\n");
        }
    }

    private static void makeFile(String path){
        var file = new File(path);
        try {
            if(file.createNewFile()) {
                log.append(LocalDateTime.now().toString() + "\t" + file.getPath() + " was created.\n");
            }else {
                log.append(LocalDateTime.now().toString() + "\t" + file.getPath() + " wasn't created.\n");
            }
        }
        catch (Exception ex) {
            log.append(LocalDateTime.now().toString() + "\t" + file.getPath() + " wasn't created.\n");
        }
    }

    private static void saveLogToTemp(){
        try(var fileWriter = new FileWriter("./Games/temp/temp.txt")){
            fileWriter.write(log.toString());
        }catch (IOException ex){
            System.out.println("Не удалось записать log в файл temp.txt");
        }
    }

    private static void createFileStructure(){
        makeDirectory("Games/src");
        makeDirectory("Games/res");
        makeDirectory("Games/savegames");
        makeDirectory("Games/temp");
        makeDirectory("Games/src/main");
        makeDirectory("Games/src/test");
        makeFile("Games/src/main/Main.java");
        makeFile("Games/src/main/Utils.java");
        makeDirectory("Games/res/drawables");
        makeDirectory("Games/res/vectors");
        makeDirectory("Games/resicons");
        makeFile("Games/temp/temp.txt");
    }

    private static void saveGame(String path, GameProgress gameProgress){
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))){
            outputStream.writeObject(gameProgress);
            System.out.println("Game progress " + path + " was saved.");
        }catch (Exception ex){
            System.out.println("Game progress can't be saved");
        }
    }

    private static void zipGame(String path, String[] objects){
        try(ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(path))){
            for (String itemPath: objects){
                File file = new File(itemPath);
                ZipEntry entry = new ZipEntry(file.getName());
                outputStream.putNextEntry(entry);
                try(FileInputStream inputStream = new FileInputStream(file)){
                    for (byte item:inputStream.readAllBytes()){
                        outputStream.write(item);
                    }
                    System.out.println("Entry " + entry.getName() + " was saved");
                }catch(Exception ex){
                    System.out.println(file + " can't be read");
                }
                outputStream.closeEntry();
            }
        }catch(Exception ex){

        }
    }

    public static void main(String[] args) {
        createFileStructure();
        saveLogToTemp();

        String saveGame1 = "Games/savegames/gameProgress1.dat";
        GameProgress gameProgress1 = new GameProgress(100, 0, 91, 1);
        saveGame(saveGame1, gameProgress1);
        GameProgress gameProgress2 = new GameProgress(50, 50, 10, 5);
        String saveGame2 = "Games/savegames/gameProgress2.dat";
        saveGame(saveGame2, gameProgress2);
        GameProgress gameProgress3 = new GameProgress(20, 100, 55, 19);
        String saveGame3 = "Games/savegames/gameProgress3.dat";
        saveGame(saveGame3, gameProgress3);

        zipGame("Games/savegames/zip.zip", new String[]{saveGame1, saveGame2, saveGame3});
    }
}
