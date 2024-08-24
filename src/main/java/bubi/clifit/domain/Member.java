//package bubi.clifit.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.UUID;
//
//@Getter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Builder
//public class Member {
//    @Id
//    @Column(name = "member_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private UUID memberId;
//
//    @Column(length = 100, unique = true, nullable = false)
//    private String email;
//
//    @Column(length = 100, nullable = false)
//    private String password;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//
//}
