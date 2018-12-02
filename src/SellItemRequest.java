public class SellItemRequest extends Request {
    private Item item;

    public SellItemRequest(Item item){
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public String getSeller(){
        return item.getSeller();
    }
}
