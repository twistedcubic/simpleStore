
public class DLinkedList<E> implements ListADT<E>{

	private Listnode<E> head; //points to head
	private int size;
	
	//constructor
	public DLinkedList(){
		this.head = new Listnode<E>(null);
		this.size = 0;
	}
	
	public void add(E item) { //add to end of list
		Listnode<E> temp = head;
		for(int i = 1; i <= size; i++){
			temp = temp.getNext();
		}
		Listnode<E> newNode = new Listnode<E>(item, null, temp);
		temp.setNext(newNode);
		size++;
	}
	//add item to list at position pos
	public void add(int pos, E item) {
		if(pos > size + 1){
			System.out.println("Error: index out of bounds");
			return;
		}
		Listnode<E> newNode = new Listnode<E>(item);

		if(pos == 1){ //if first node;
			newNode.setNext(head.getNext());
			head.setNext(newNode); 
			newNode.setPrev(head);
		}
		else{
			Listnode<E> temp = head;
			int count = 1;
			while(count < pos){
				temp = temp.getNext(); 
				count++;
			}
			newNode.setPrev(temp);
			newNode.setNext(temp.getNext());
			temp.setNext(newNode);
		}
		size++;
	}

	//whether list contains item
	public boolean contains(E item) {
		boolean found = false;
		Listnode<E> temp = head;
		
		for(int i = 1; i <= size; i++){
			if(temp.equals(item)){
				found = true;
				break;
			}
			temp = temp.getNext();
		}		
		return found;
	}
	//get item at position pos
	public E get(int pos) {
		if(pos > size){
			System.out.println("Error: index out of bounds");
			return null;
		}
		Listnode<E> temp = head.getNext();
		for(int i = 1; i < pos; i++){
			temp = temp.getNext();
		}
		return temp.getData();
	}
	//whether list is empty
	public boolean isEmpty() {
		if(size == 0)
			return true;
		else
			return false;
	}
	//remove item at position pos
	public E remove(int pos) {
		if(pos > size){
			System.out.println("Error: index out of bounds");
			return null;
		}
		Listnode<E> temp = head;
	
		for(int i = 1; i < pos; i++){
			temp = temp.getNext(); 
		}
		Listnode<E> next = temp.getNext().getNext();
		E data = temp.getNext().getData();
		next.setPrev(temp);	
		temp.setNext(next);
		size--;
		return data;
	}
	//returns size of list
	public int size() {
		return size;
	}

}
