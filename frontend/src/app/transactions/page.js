// "use client";

// import { useState, useEffect } from "react";

// export default function TransactionsPage() {
//   const [transactions, setTransactions] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     fetch("/api/transactions", { credentials: "include" })
//       .then((res) => {
//         if (!res.ok) throw new Error(`Server responded ${res.status}`);
//         return res.json();
//       })
//       .then((data) => setTransactions(data))
//       .catch((err) => {
//         console.error(err);
//         setError("Failed to load transactions.");
//       })
//       .finally(() => setLoading(false));
//   }, []);

//   if (loading) {
//     return (
//       <main className="min-h-screen flex items-center justify-center bg-background text-foreground">
//         <p>Loading transactions…</p>
//       </main>
//     );
//   }

//   if (error) {
//     return (
//       <main className="min-h-screen flex items-center justify-center bg-background text-foreground">
//         <p className="text-destructive">{error}</p>
//       </main>
//     );
//   }

//   return (
//     <main className="p-6 bg-background text-foreground">
//       <h1 className="text-2xl font-semibold mb-4">Your Transactions</h1>
//       <div className="overflow-x-auto">
//         <table className="min-w-full bg-card border border-border">
//           <thead className="bg-secondary/10">
//             <tr>
//               <th className="px-4 py-2 text-left">Date</th>
//               <th className="px-4 py-2 text-right">Amount</th>
//               <th className="px-4 py-2 text-left">Category</th>
//               <th className="px-4 py-2 text-left">Merchant</th>
//               <th className="px-4 py-2 text-left">Description</th>
//             </tr>
//           </thead>
//           <tbody>
//             {transactions.map((tx) => (
//               <tr key={tx.id} className="border-t border-border">
//                 <td className="px-4 py-2">
//                   {new Date(tx.date).toLocaleDateString()}
//                 </td>
//                 <td className="px-4 py-2 text-right">
//                   ${tx.amount.toFixed(2)}
//                 </td>
//                 <td className="px-4 py-2">{tx.category}</td>
//                 <td className="px-4 py-2">{tx.merchant}</td>
//                 <td className="px-4 py-2">{tx.description}</td>
//               </tr>
//             ))}
//           </tbody>
//         </table>
//       </div>
//     </main>
//   );
// }

"use client";

import { useState, useEffect } from "react";

import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/components/ui/table";
import { ScrollArea } from "@/components/ui/scroll-area";

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  /* fetch once on mount */
  useEffect(() => {
    fetch("/api/transactions", { credentials: "include" })
      .then((res) => {
        if (!res.ok) throw new Error(`Server responded ${res.status}`);
        return res.json();
      })
      .then(setTransactions)
      .catch((err) => {
        console.error(err);
        setError("Failed to load transactions.");
      })
      .finally(() => setLoading(false));
  }, []);

  /* ------------- status screens ------------- */
  if (loading)
    return (
      <main className="min-h-screen flex items-center justify-center">
        Loading transactions…
      </main>
    );

  if (error)
    return (
      <main className="min-h-screen flex items-center justify-center">
        <p className="text-destructive">{error}</p>
      </main>
    );

  /* ------------- UI ------------- */
  return (
    <main className="p-6 space-y-6">
      <h1 className="text-2xl font-semibold">Your Transactions</h1>

      <Card>
        <CardHeader>
          <CardTitle>Recent Activity</CardTitle>
        </CardHeader>

        <CardContent className="p-0">
          {/* horizontal & vertical scroll without breaking card border */}
          <ScrollArea className="w-full">
            <div className="min-w-[720px]">
              {" "}
              {/* keep headers visible on small screens */}
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Date</TableHead>
                    <TableHead className="text-right">Amount</TableHead>
                    <TableHead>Category</TableHead>
                    <TableHead>Merchant</TableHead>
                    <TableHead>Description</TableHead>
                  </TableRow>
                </TableHeader>

                <TableBody>
                  {transactions.map((tx) => (
                    <TableRow key={tx.id}>
                      <TableCell>
                        {new Date(tx.date).toLocaleDateString()}
                      </TableCell>
                      <TableCell className="text-right">
                        ${tx.amount.toFixed(2)}
                      </TableCell>
                      <TableCell>{tx.category}</TableCell>
                      <TableCell>{tx.merchant}</TableCell>
                      <TableCell>{tx.description}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </ScrollArea>
        </CardContent>
      </Card>
    </main>
  );
}
