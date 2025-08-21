package mcdonald.model.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class QueryLoader {

    private static final String FILE_PATH = "Database" + File.separator + "Query.sql";

    public static String loadQuery(String queryName) throws Exception {
        StringBuilder query = new StringBuilder();
        boolean found = false;

        if (!Files.exists(Paths.get(FILE_PATH))) {
            throw new Exception("Query file not found: " + FILE_PATH);
        }

        for (String line : Files.readAllLines(Paths.get(FILE_PATH))) {
            if (line.trim().equalsIgnoreCase("-- " + queryName)) {
                found = true;
                continue;
            }
            if (found) {
                if (line.trim().startsWith("--") && !line.trim().equalsIgnoreCase("-- " + queryName)) break;
                if (!line.trim().isEmpty()) query.append(line).append(" ");
            }
        }
        return query.toString().trim();
    }
}
