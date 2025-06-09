// "use client";

// import { useState, useEffect } from "react";
// import { Button } from "@/components/ui/button";

// export default function GoalsPage() {
//   const [goal, setGoal] = useState(null);
//   const [editing, setEditing] = useState(false);
//   const [form, setForm] = useState({
//     targetAmount: "",
//     categoryFocus: "",
//     startDate: "",
//     endDate: "",
//   });
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState("");

//   // Load existing goal
//   useEffect(() => {
//     fetch("/api/goals", { credentials: "include" })
//       .then((res) => {
//         if (res.status === 404) return null;
//         if (!res.ok) throw new Error(`Server responded ${res.status}`);
//         return res.json();
//       })
//       .then((data) => {
//         if (data) {
//           setGoal(data);
//           setForm({
//             targetAmount: data.targetAmount,
//             categoryFocus: data.categoryFocus,
//             startDate: data.startDate.slice(0, 10),
//             endDate: data.endDate.slice(0, 10),
//           });
//         }
//       })
//       .catch((err) => {
//         console.error(err);
//         setError("Failed to load your goal.");
//       })
//       .finally(() => setLoading(false));
//   }, []);

//   async function handleSubmit(e) {
//     e.preventDefault();
//     setLoading(true);
//     setError("");
//     try {
//       const res = await fetch("/api/goals", {
//         method: goal ? "POST" : "POST",
//         credentials: "include",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({
//           targetAmount: parseFloat(form.targetAmount),
//           categoryFocus: form.categoryFocus,
//           startDate: new Date(form.startDate).toISOString(),
//           endDate: new Date(form.endDate).toISOString(),
//         }),
//       });
//       if (!res.ok) throw new Error(`Server responded ${res.status}`);
//       const saved = await res.json();
//       setGoal(saved);
//       setEditing(false);
//     } catch (err) {
//       console.error(err);
//       setError("Failed to save your goal.");
//     } finally {
//       setLoading(false);
//     }
//   }

//   if (loading) {
//     return (
//       <main className="min-h-screen flex items-center justify-center bg-background text-foreground">
//         <p>Loading goal…</p>
//       </main>
//     );
//   }

//   return (
//     <main className="p-6 bg-background text-foreground max-w-md">
//       <h1 className="text-2xl font-semibold mb-4">Your Savings Goal</h1>

//       {error && <p className="text-destructive mb-4">{error}</p>}

//       {!goal || editing ? (
//         <form onSubmit={handleSubmit} className="space-y-4">
//           <div>
//             <label className="block mb-1">Target Amount</label>
//             <input
//               type="number"
//               required
//               value={form.targetAmount}
//               onChange={(e) =>
//                 setForm({ ...form, targetAmount: e.target.value })
//               }
//               className="w-full px-3 py-2 border rounded"
//             />
//           </div>
//           <div>
//             <label className="block mb-1">Category Focus</label>
//             <input
//               type="text"
//               required
//               value={form.categoryFocus}
//               onChange={(e) =>
//                 setForm({ ...form, categoryFocus: e.target.value })
//               }
//               className="w-full px-3 py-2 border rounded"
//             />
//           </div>
//           <div>
//             <label className="block mb-1">Start Date</label>
//             <input
//               type="date"
//               required
//               value={form.startDate}
//               onChange={(e) => setForm({ ...form, startDate: e.target.value })}
//               className="w-full px-3 py-2 border rounded"
//             />
//           </div>
//           <div>
//             <label className="block mb-1">End Date</label>
//             <input
//               type="date"
//               required
//               value={form.endDate}
//               onChange={(e) => setForm({ ...form, endDate: e.target.value })}
//               className="w-full px-3 py-2 border rounded"
//             />
//           </div>
//           <Button type="submit">{goal ? "Update Goal" : "Create Goal"}</Button>
//         </form>
//       ) : (
//         <div className="space-y-2">
//           <p>
//             You plan to save <strong>${goal.targetAmount.toFixed(2)}</strong> on{" "}
//             <strong>{goal.categoryFocus}</strong> between{" "}
//             <strong>{new Date(goal.startDate).toLocaleDateString()}</strong> and{" "}
//             <strong>{new Date(goal.endDate).toLocaleDateString()}</strong>.
//           </p>
//           <Button variant="outline" onClick={() => setEditing(true)}>
//             Edit Goal
//           </Button>
//         </div>
//       )}
//     </main>
//   );
// }

"use client";

import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";

import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export default function GoalsPage() {
  /* -------------------------------------------------- state */
  const [goal, setGoal] = useState(null); // saved goal from API
  const [editing, setEdit] = useState(false); // toggles form vs summary
  const [loading, setLoad] = useState(true);
  const [error, setError] = useState("");

  /* React-Hook-Form setup */
  const form = useForm({
    defaultValues: {
      targetAmount: "",
      categoryFocus: "",
      startDate: "",
      endDate: "",
    },
  });

  /* -------------------------------------------------- load existing goal */
  useEffect(() => {
    fetch("/api/goals", { credentials: "include" })
      .then((r) =>
        r.status === 404 ? null : r.ok ? r.json() : Promise.reject(r.status)
      )
      .then((data) => {
        if (data) {
          setGoal(data);
          form.reset({
            targetAmount: data.targetAmount,
            categoryFocus: data.categoryFocus,
            startDate: data.startDate.slice(0, 10),
            endDate: data.endDate.slice(0, 10),
          });
        }
      })
      .catch((err) => {
        console.error(err);
        setError("Failed to load your goal.");
      })
      .finally(() => setLoad(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /* -------------------------------------------------- submit */
  async function onSubmit(values) {
    setLoad(true);
    setError("");
    try {
      const res = await fetch("/api/goals", {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          targetAmount: parseFloat(values.targetAmount),
          categoryFocus: values.categoryFocus,
          startDate: new Date(values.startDate).toISOString(),
          endDate: new Date(values.endDate).toISOString(),
        }),
      });
      if (!res.ok) throw new Error(res.status);
      const saved = await res.json();
      setGoal(saved);
      setEdit(false);
    } catch (err) {
      console.error(err);
      setError("Failed to save your goal.");
    } finally {
      setLoad(false);
    }
  }

  /* -------------------------------------------------- early states */
  if (loading)
    return (
      <main className="min-h-screen flex items-center justify-center">
        Loading goal…
      </main>
    );

  /* -------------------------------------------------- UI */
  return (
    <main className="p-6 flex justify-center">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>Your Savings Goal</CardTitle>
        </CardHeader>

        {error && <p className="text-destructive text-sm px-6">{error}</p>}

        {/* show form when no goal or editing */}
        {!goal || editing ? (
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)}>
              <CardContent className="space-y-4">
                <FormField
                  control={form.control}
                  name="targetAmount"
                  rules={{ required: true }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Target Amount</FormLabel>
                      <FormControl>
                        <Input
                          type="number"
                          step="0.01"
                          placeholder="1000"
                          {...field}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="categoryFocus"
                  rules={{ required: true }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Category Focus</FormLabel>
                      <FormControl>
                        <Input placeholder="Travel" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="startDate"
                  rules={{ required: true }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Start Date</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="endDate"
                  rules={{ required: true }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>End Date</FormLabel>
                      <FormControl>
                        <Input type="date" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </CardContent>

              <CardFooter className="justify-end gap-2">
                {goal && (
                  <Button
                    type="button"
                    variant="ghost"
                    onClick={() => {
                      form.reset({
                        targetAmount: goal.targetAmount,
                        categoryFocus: goal.categoryFocus,
                        startDate: goal.startDate.slice(0, 10),
                        endDate: goal.endDate.slice(0, 10),
                      });
                      setEdit(false);
                    }}
                  >
                    Cancel
                  </Button>
                )}
                <Button type="submit" disabled={loading}>
                  {goal ? "Update Goal" : "Create Goal"}
                </Button>
              </CardFooter>
            </form>
          </Form>
        ) : (
          /* summary view */
          <>
            <CardContent className="space-y-2">
              <p>
                You plan to save{" "}
                <strong>${goal.targetAmount.toFixed(2)}</strong> on{" "}
                <strong>{goal.categoryFocus}</strong> between{" "}
                <strong>{new Date(goal.startDate).toLocaleDateString()}</strong>{" "}
                and{" "}
                <strong>{new Date(goal.endDate).toLocaleDateString()}</strong>.
              </p>
            </CardContent>
            <CardFooter>
              <Button variant="outline" onClick={() => setEdit(true)}>
                Edit Goal
              </Button>
            </CardFooter>
          </>
        )}
      </Card>
    </main>
  );
}
