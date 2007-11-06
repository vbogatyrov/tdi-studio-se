// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.components.thash;

import gnu.trove.THashMap;
import gnu.trove.TObjectHashingStrategy;

import java.util.Map;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public class ReliabilityHashMapTest {

    public static void main(String[] args) throws Exception {

        // int loop = 100;
        int loop = 5000000;

        Sizeof.runGC();
        long heap1 = Sizeof.usedMemory(); // Take a before heap snapshot

        long time1 = System.currentTimeMillis();

        TObjectHashingStrategy objectHashingStrategy = new TObjectHashingStrategy() {

            /*
             * (non-Javadoc)
             * 
             * @see gnu.trove.TObjectHashingStrategy#computeHashCode(java.lang.Object)
             */
            @Override
            public int computeHashCode(Object o) {
                return o == null ? 0 : o.hashCode();
            }

            /*
             * (non-Javadoc)
             * 
             * @see gnu.trove.TObjectHashingStrategy#equals(java.lang.Object, java.lang.Object)
             */
            @Override
            public boolean equals(Object o1, Object o2) {
                return o2 == null ? o1 == null : o2.equals(o1);
            }

        };

        // Tests for 5 000 000 items
        
        // ################################################################################
        // Do not compare bytes number in this class with bytes number MemoryHashMapTest, 
        // the purpose of this test class is not to measure heap memory !
        // ################################################################################

        // Map hashMap = new HashMap(); // 142 bytes, 94 s
        // Map hashMap = new HashMap(loop, 1f); // ??
//         Map hashMap = new THashMap(objectHashingStrategy); // 132 bytes, 25 s
        // Map hashMap = new THashMap(loop, 0.1f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(loop, 1f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(loop + 10000, 1.0f, objectHashingStrategy); // ??
        // Map hashMap = new THashMap(loop + 10000, 0.99f, objectHashingStrategy); // ??
        Map hashMap = new THashMap(10000, 1.0f, objectHashingStrategy); // 119 bytes , 34 s

        System.out.println("Write step");
        for (int i = 0; i < loop; i++) {
            // => bean from tFileInput for example...
            Bean bean = new Bean(i, String.valueOf(i));
            
            // => THash storing step
            KeyForMap keyForMap = new KeyForMap(i, bean.hashCode());
            hashMap.put(keyForMap, keyForMap);
            DB.put(null, i, bean);
            
            if (i % 100000 == 0) {
                System.out.println("Writing " + i);
            }
        }

        System.out.println("Read step");
        for (int i = 0; i < loop; i++) {
            if (i % 100000 == 0) {
                System.out.println("Reading " + i);
            }
            
            
            // => bean from main flow in tMap for example...
            Bean bean = new Bean(i, String.valueOf(i));
            
            // => search properties of bean in lookup for example...
            KeyForMap keyForMap = (KeyForMap) hashMap.get(bean);

            // validity test
            if (keyForMap == null) {
                throw new RuntimeException("keyForMap not found with id " + i);
            }

            // => bean found from DB
            Bean beanFromDB = (Bean) DB.get(null, keyForMap.dbIdBean);
            
            // validity test
            if (beanFromDB == null) {
                throw new RuntimeException("Bean not found with id " + keyForMap.dbIdBean);
            }
            // validity test
            if (!beanFromDB.name.equals(bean.name) || beanFromDB.primitiveInt != bean.primitiveInt) {
                throw new RuntimeException("Values of beans are different with id " + keyForMap.dbIdBean);
            }
        }

        long time2 = System.currentTimeMillis();

        System.out.println("waiting for garbage collector...");
        Sizeof.runGC();
        long heap2 = Sizeof.usedMemory(); // Take a before heap snapshot

        final int size = Math.round(((float) (heap2 - heap1)) / loop);
        System.out.println("'before' heap: " + heap1 + " bytes, 'after' heap: " + heap2 + " bytes "); // not needed
        // here
        System.out.println("heap delta: " + (heap2 - heap1) + " bytes ");
        System.out.println("size by item: " + size + " bytes ");
        System.out.println("Number of loops: " + loop);
        System.out.println("Number of items: " + hashMap.size());
        if (loop != hashMap.size()) {
            System.out.println("WARNING: loops number is different of items number !");
        }
        System.out.println("Time: " + ((time2 - time1) / 1000) + " s");

    }

}
