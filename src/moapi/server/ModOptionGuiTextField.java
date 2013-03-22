package moapi.server;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import moapi.*;

/**
* A MOAPI text field representation to allow the storage of the option inside
*
* @author Jonathan Brazell
* @version	1.4.7
*/
public class ModOptionGuiTextField extends JTextField {
	/** The option for this button */
	protected ModOption option = null;

	public ModOptionGuiTextField(ModOption option) {
		super();
		int maxLen = ((ModOptionText)option).getMaxLength();
		if (maxLen>0)
			setDocument(new MaxLenDocument(maxLen));
		setText(((ModOptionText)option).getValue(false));
		if ((maxLen<20) && (maxLen!=0))
			setColumns(maxLen);
		else
			setColumns(20);
		this.option = option;
		getDocument().addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent e) { save(); }
			public void removeUpdate(DocumentEvent e) { save(); }
			public void insertUpdate(DocumentEvent e) { save(); }
			public void save() { getOption().setValue(getText(), false); }
		});		
	}

	static class MaxLenDocument extends PlainDocument {
		private int maxLen;
		
		public MaxLenDocument(int maxLength) {
			maxLen = maxLength;
		}
		
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			if (getLength() + str.length() > maxLen)
    		      java.awt.Toolkit.getDefaultToolkit().beep();
    		    else
    		      super.insertString(offset, str, a);
        }
    }	
	
	/**
	* Get the option this button represents
	*
	* @return	option this button represents
	*/
	public ModOption getOption() {
		return option;
	}
}
