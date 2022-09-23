public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        int i = 0;
        Deque<Character> deque = new LinkedListDeque<>();
        char ch;
        while (i < word.length()){
            ch = word.charAt(i);
            deque.addLast(ch);
            i ++;
        }
        return deque;
    }

    public boolean isPalindrome(String word){
        return helpisPalindrome(word);
    }
    private boolean helpisPalindrome(String word){
        String wordcopy = word;
        Deque<Character> deque = wordToDeque(wordcopy);
        while(deque.size() > 1){
            if(deque.removeFirst() != deque.removeLast()){
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc){
        if (word == null || word.length() <= 1){
            return true;
        }
        for (int i = 0; i < word.length() / 2; i++){
            if (!cc.equalChars(word.charAt(i), word.charAt(word.length() - i - 1))){
                return false;
            }
        }
        return true;
    }

}
