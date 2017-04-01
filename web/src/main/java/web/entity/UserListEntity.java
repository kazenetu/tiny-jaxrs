package web.entity;

public class UserListEntity {

    private String searchUserId;

    private int pageIndex;

    private String sortKey;

    private String sortType;

    public UserListEntity(){
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return sortKey
     */
    public String getSortKey() {
        return sortKey;
    }

    /**
     * @param sortKey セットする sortKey
     */
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    /**
     * @return sortType
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * @param sortType セットする sortType
     */
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    /**
     * @return searchUserId
     */
    public String getSearchUserId() {
        return searchUserId;
    }

    /**
     * @param searchUserId セットする searchUserId
     */
    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }
}
