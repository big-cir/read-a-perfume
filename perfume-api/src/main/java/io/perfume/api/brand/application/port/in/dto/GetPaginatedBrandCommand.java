package io.perfume.api.brand.application.port.in.dto;

public class GetPaginatedBrandCommand {
  private int page;

  private int size;

  public int page() {
    return page - 1;
  }

  public GetPaginatedBrandCommand(int page, int size) {
    this.page = page;
    this.size = size;
  }

  public int offset() {
    return (page - 1) * size;
    // return page - 1;
  }

  public int limit() {
    return size;
  }
}
