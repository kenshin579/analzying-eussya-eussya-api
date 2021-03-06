package com.sproutt.eussyaeussyaapi.domain.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MemberDTO;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    private String memberId;

    @Column
    private String password;

    @Email
    @Column(nullable = true)
    private String email;

    @Column(unique = true)
    private String nickName;

    @Column
    private String authentication;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Mission> missions = new ArrayList<>();

    @Builder
    public Member(String memberId, String password, String email, String nickName, String authentication, Provider provider) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.authentication = authentication;
        this.provider = provider;
    }

    public void changeAuthCode(String authCode) {
        this.authentication = authCode;
    }

    public JwtMemberDTO toJwtInfo() {
        return JwtMemberDTO.builder()
                           .id(this.id)
                           .memberId(this.memberId)
                           .nickName(this.nickName)
                           .build();
    }

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public boolean isSame(Member member) {
        return this == member;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", authentication='" + authentication + '\'' +
                ", provider=" + provider +
                '}';
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                        .id(this.id)
                        .memberId(this.memberId)
                        .nickName(this.nickName)
                        .build();
    }

    public void verifyEmail() {
        this.authentication = "Y";
    }

    public boolean isVerified() {
        return this.authentication.equals("Y");
    }
}
