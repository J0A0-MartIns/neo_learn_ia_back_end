package neo_learn_ia_api.Neo.Learn.Ia.API.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "origin")
    private String origin;

    @Lob
    private byte[] data;


    public FileEntity(String fileName, String fileType, String origin, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.origin = origin;
        this.data = data;
    }

}
