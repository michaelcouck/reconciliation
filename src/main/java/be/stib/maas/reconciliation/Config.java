package be.stib.maas.reconciliation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Michael Couck
 * @since 19-07-2022
 */
@Getter
@Setter
@Configuration
public class Config {

    @Value("{maas.data-directory}")
    private String dataDirectory;
    @Value("{maas.deleted-directory}")
    private String deletedDirectory;
    @Value("{maas.delete-files:false}")
    private boolean deleteFiles;

    @Value("{maas.fileSystemDataSets}")
    private List<String> fileSystemDataSets;

    public List<String> getFilePaths() {
        return getFileSystemDataSets().stream().map(f -> getDataDirectory() + f).collect(Collectors.toList());
    }

    public List<String> getDeletedFilePaths() {
        return getFileSystemDataSets().stream().map(f -> getDeletedDirectory() + f).collect(Collectors.toList());
    }

}