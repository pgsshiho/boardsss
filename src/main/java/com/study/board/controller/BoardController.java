package com.study.board.controller;

import com.study.board.BoardApplication;
import com.study.board.entity.board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") //localhost:8087/board/write
    public String boardWriteForm(){

        return "boardwrite";
    }
    @PostMapping("/board/writepro")
    public String boardWritePro(board Board, Model model, @RequestParam("file") MultipartFile file) throws Exception{
        boardService.write(Board, file  );
        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }
    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(value = "searchKeyword", required = false)String searchKeyword) {
        Page<board> list;
        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    // BoardController.java
    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam("id") Integer id) {
        board board = boardService.boardView(id);
        if (board == null) {
            return "redirect:/board/list"; // 데이터가 없으면 리스트 페이지로 리디렉션
        }
        model.addAttribute("board", board);
        return "boardview";
    }
    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam("id") Integer id, Model model) {
        boardService.boardDelete(id);
        model.addAttribute("message", "글이 삭제되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "redirect:/board/list?page=0";
    }
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        boardService.boardDelete(id);
        model.addAttribute("message", "글이 삭제되었습니다.");
        model.addAttribute("searchUrl", "/board/list?page=0");
        return "redirect:/board/list?page=0"; // 삭제 후 리스트 첫 페이지로 리디렉션
    }
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, board board, @RequestParam("file") MultipartFile file) throws Exception {
        board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);
        return "redirect:/board/list";
    }
}
