package com.springboot.board.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages){
        // 페이지네이션의 양 끝 번호를 계산.
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0); // Math에 max메서드로 0보다 큰수를 사용함으로 인해 음수를 막는다.
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages); // min메서드로 전체 페이지보다 더 길게 나오는 것을 막는다.

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }

}
