package com.anytime.studymaker.domain.study;

import com.anytime.studymaker.controller.dto.StudyApiResponse;
import com.anytime.studymaker.domain.category.Category;
import com.anytime.studymaker.domain.userStudy.UserStudy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@Entity
public class Study {
    @Id
    @GeneratedValue
    private Long studyId;
    private String studyName;
    private Integer studyMaximum;
    private String studySummary;
    private String studyDescription;
    private String studyImage;
    private Boolean studyStatus;
    private Boolean studyType;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    private Region region;

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
    List<UserStudy> userStudyList = new ArrayList<>();

    public StudyApiResponse toApiResponse() {
        UserStudy managerInfo = null;
        int numberOfMember = 0;
        for (UserStudy userStudy : userStudyList) {
            // UserStatus status = userStudy.getStatus().getUserStatus();
            // if (status == UserStatus.MANAGER) {
            //     managerInfo = userStudy;
            // }
            // if (status != UserStatus.BANNED) {
            //     numberOfMember++;
            // }
            if(userStudy.getStatus().getStatusId() == 0){
                managerInfo = userStudy;
            }
            if (userStudy.getStatus().getStatusId() != 3) {
                numberOfMember++;
            }
        }

        return StudyApiResponse.builder()
                .studyId(studyId).studyName(studyName).studyMaximum(studyMaximum)
                .studySummary(studySummary).studyDescription(studyDescription)
                .studyImage(studyImage).studyStatus(studyStatus).studyType(studyType)

                .managerId(managerInfo.getUser().getUserId())
                .manager(managerInfo.getUser().getNickname())
                .numberOfMember(numberOfMember)

                .categoryId(category.getCategoryId())
                .category(category.getCategoryName())

                .regionId(region.getRegionId())
                .region(region.getRegionName())

                .createAt(createAt)
                .updateAt(updateAt).build();
    }
   
}
