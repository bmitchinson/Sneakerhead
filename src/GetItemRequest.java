public class GetItemRequest extends Request {
    private Item item;

    public GetItemRequest(Item item){
        this.item = item;
    }

    public int getItem() {
        return item.getId();
    }
}
