package com.github.prgrms.orders;

import com.github.prgrms.security.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.github.prgrms.utils.ApiUtils.ApiResult;
import static com.github.prgrms.utils.ApiUtils.success;

@RestController
@RequestMapping("api/orders")
public class ReviewRestController {
    // TODO review �޼ҵ� ������ �ʿ��մϴ�.

    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{id}/review")
    public ApiResult<Review> review(Authentication authentication, @PathVariable("id") Long orderId,
            @RequestBody @Valid ReviewDto request) {

        // ����
        JwtAuthentication principal = (JwtAuthentication) authentication.getPrincipal();

        return success(reviewService.review(principal.id, orderId, request));
    }
}