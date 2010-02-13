/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.pecoff4j.asm;

public class PUSH extends AbstractInstruction
{
    private int register;
    private byte imm8;

    public PUSH(int register) {
        this.register = register;
        this.code = toCode(0x50 | register);
    }

    public PUSH(byte imm8) {
        this.imm8 = imm8;
        this.code = toCode(0x6a, imm8);
    }

    public String toIntelAssembly() {
        switch (getOpCode()) {
        case 0x6a:
            return "push " + toHexString(imm8, false);
        }
        return "push " + Register.to32(register);
    }
}