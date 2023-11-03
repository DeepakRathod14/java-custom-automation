
package framework.bean.users.get.userall;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import framework.bean.AbstractDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAllDto extends AbstractDto {

	@JsonProperty(value = "page", access = JsonProperty.Access.READ_WRITE)
	private Integer page;
	@JsonProperty(value = "per_page", access = JsonProperty.Access.READ_WRITE)
	private Integer perPage;
	@JsonProperty(value = "total", access = JsonProperty.Access.READ_WRITE)
	private Integer total;
	@JsonProperty(value = "total_pages", access = JsonProperty.Access.READ_WRITE)
	private Integer totalPages;
	@JsonProperty(value = "data", access = JsonProperty.Access.READ_WRITE)
	private List<Datum> data = null;
	@JsonProperty(value = "support", access = JsonProperty.Access.READ_WRITE)
	private Support support;
	
	@JsonProperty("page")
	public Integer getPage() {
		return page;
	}

	@JsonProperty("page")
	public void setPage(Integer page) {
		this.page = page;
	}

	@JsonProperty("per_page")
	public Integer getPerPage() {
		return perPage;
	}

	@JsonProperty("per_page")
	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}

	@JsonProperty("total")
	public Integer getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(Integer total) {
		this.total = total;
	}

	@JsonProperty("total_pages")
	public Integer getTotalPages() {
		return totalPages;
	}

	@JsonProperty("total_pages")
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@JsonProperty("data")
	public List<Datum> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<Datum> data) {
		this.data = data;
	}

	@JsonProperty("support")
	public Support getSupport() {
		return support;
	}

	@JsonProperty("support")
	public void setSupport(Support support) {
		this.support = support;
	}

}
