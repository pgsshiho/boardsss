package com.study.board.service;

import com.study.board.entity.board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    //글 작성
    public void write(board board, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        if (file != null && !file.isEmpty()) {  // 파일이 존재하는 경우에만 처리
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);
            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        }
        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public Page<board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
    public Page<board> boardSearchList(String searchKeyword,Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword,pageable);
    }

    public board boardView(Integer id) {
        Optional<board> board = boardRepository.findById(id);
        return board.orElse(null); // 데이터가 없으면 null 반환
    }
    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
        reorderBoardNumbers();
    }
    private void reorderBoardNumbers() {
        // ID 순으로 정렬하여 모든 게시글 가져오기
        List<board> boards = boardRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        // boardNumber를 1부터 시작하도록 재정렬
        for (int i = 0; i < boards.size(); i++) {
            boards.get(i).setBoardNumber(i + 1); // boardNumber 설정
            boardRepository.save(boards.get(i)); // 변경 사항 저장
        }
    }

}
