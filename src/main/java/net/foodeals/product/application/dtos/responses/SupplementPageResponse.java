package net.foodeals.product.application.dtos.responses;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.product.domain.entities.Supplement;

@NoArgsConstructor
@Setter
@Getter
public class SupplementPageResponse {

	private int totalPages;
    private int currentPage;
    private List<Supplement> content;
    private long totalOfType;

    public SupplementPageResponse(Page<Supplement> page,long totalOfType) {
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.content = page.getContent();
        this.totalOfType=totalOfType;
    }
}
