package com.study.board.repository;

import com.study.board.entity.board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<board, Integer> {
    // 검색을 위한 메서드 선언
    Page<board> findByTitleContaining(String keyword, Pageable pageable);
}
