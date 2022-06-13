package paw.project.backend_server.Service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class FileService {
    final String ProfileImagesDirectory = "ProfilePictures/";

    public boolean saveFile(String filename, byte[] bytes)
    {
        try {
            System.out.println("Dimensiune fisier(bytes): " + bytes.length);

            String subFolder = filename.split("\\.", 2)[0];

            //daca folder-ul nu exsita, se va crea
            createDirectory( ProfileImagesDirectory + subFolder);

            //stergem vechiul continut al folder-ului
            purgeDirectory(new File( ProfileImagesDirectory + subFolder));

            //scriu noul fisier
            OutputStream os = new FileOutputStream(   ProfileImagesDirectory + subFolder + "/" + filename);
            os.write(bytes);
            os.close();

            return true;
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
            return false;
        }
    }

    public byte[] getImage(int userId) throws IOException {
        String dirPath = ProfileImagesDirectory + "User_" +  userId;

        File file = new File(dirPath);

        file = findFile("User_" + userId, file);

        return Files.readAllBytes(file.toPath());
    }

    public File findFile(String name,File file)
    {
        File[] list = file.listFiles();
        if(list!=null) {
            for (File fil : list) {
                if (fil.isDirectory()) {
                    findFile(name, fil);
                } else if (fil.getName().contains(name)) {
                    return fil;
                }
            }
        }

        return null;
    }

    private void purgeDirectory(File dir) {
        for (File file: dir.listFiles()) {
            if (file.isDirectory())
                purgeDirectory(file);
            file.delete();
        }
    }
    private boolean createDirectory(String path)
    {
        System.out.println("Se creaza folder-ul " + path);
        return new File(path).mkdirs();
    }
}
