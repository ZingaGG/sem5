import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        {
            String sourceDir = "testPath"; // Укажите путь к исходной директории
            String backupDir = "./backup"; // Папка для резервной копии

            try {
                backupFiles(sourceDir, backupDir);
                System.out.println("Резервная копия файлов успешно создана.");
            } catch (IOException e) {
                System.err.println("Ошибка при создании резервной копии: " + e.getMessage());
            }
        }
        {
            int[] field = {0, 1, 2, 3, 1, 2, 0, 3, 2};
            try {
                FileOutputStream fos = new FileOutputStream("field.txt");
                packAndWrite(field, fos);
                fos.close();
                System.out.println("Запись в файл завершена.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void backupFiles(String sourceDir, String backupDir) throws IOException {
        File sourceDirectory = new File(sourceDir);
        File backupDirectory = new File(backupDir);

        // Проверяем, существует ли исходная директория
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            throw new IllegalArgumentException("Исходная директория не существует или не является директорией.");
        }

        // Создаем папку для резервной копии, если она не существует
        if (!backupDirectory.exists()) {
            backupDirectory.mkdirs();
        }

        File[] files = sourceDirectory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                File backupFile = new File(backupDirectory, file.getName());
                Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static void packAndWrite(int[] field, FileOutputStream fos) throws IOException {
        int packedValue = 0;
        int bitsWritten = 0;

        for (int i = 0; i < field.length; i++) {
            // Упаковываем каждое значение в 2 бита
            packedValue = (packedValue << 2) | field[i];
            bitsWritten += 2;

            if (bitsWritten == 8) {
                fos.write(packedValue);
                packedValue = 0;
                bitsWritten = 0;
            }
        }

        // Если остались незаписанные биты, дополняем их и записываем
        if (bitsWritten > 0) {
            packedValue = packedValue << (8 - bitsWritten);
            fos.write(packedValue);
        }
    }
}
