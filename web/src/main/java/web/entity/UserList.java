package web.entity;

public class UserList {

    private String searchUserId;

    public UserList(){
    }

    public UserList(String searchUserId){
        this.searchUserId = searchUserId;
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
