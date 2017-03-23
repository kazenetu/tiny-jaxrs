package web.entity;

public class UserListEntity {

    private String searchUserId;

    private int pageIndex;

    public UserListEntity(){
    }

    public UserListEntity(int pageIndex, String searchUserId){
        this.setPageIndex(pageIndex);
        this.searchUserId = searchUserId;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
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