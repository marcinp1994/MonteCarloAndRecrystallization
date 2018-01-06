
package rekrystalizacja;

import java.awt.Color;


public class Punkt 
{

        //private Color color;


       // private  int Id=0;
        private int x;
        private int y;
        
        public Punkt(int a, int b)
        {
            
            x=a;
            y=b;
            
        }
        
        public boolean equals(Punkt p) {
		if (this.x==p.x && this.y==p.y) return true;
		return false;
	}
	
        
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }

    public void setY(int Y) {
        this.y = y;
    }
        
    
    @Override
   public String toString()
   {
       return "X: " + getX() + " Y: " + getY() + " ID: " ;
   }        
        
        
}
