package com.anytime.studymaker.domain.study.repository;

import com.anytime.studymaker.domain.study.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
