package hu.ponte.hr.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Image {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String mimeType;

    @Column
    private String path;

    @Column
    private long size;

    @Column
    private String digitalSign;

}
