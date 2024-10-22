package backend.academy.path.impl;

import backend.academy.path.PathHandler;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LocalPathHandler implements PathHandler {
    @Override
    public List<Map.Entry<String, Stream<String>>> handlePath(String path) throws IOException {
        List<Map.Entry<String, Stream<String>>> matchesList = new ArrayList<>();
        Path rootDir = Path.of("C:\\backend_academy_2024_project_3-java-salex06\\src\\main\\resources");
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) throws IOException {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher(path);
                Path name = file.getFileName();
                if (matcher.matches(file.toAbsolutePath())) {
                    matchesList.add(Map.entry(name.toString(), Files.lines(file)));
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(rootDir, matcherVisitor);
        return matchesList;
    }
}
