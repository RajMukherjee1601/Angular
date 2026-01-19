export type PaymentMode = 'ONLINE' | 'OFFLINE';
export type PaymentStatus = 'PENDING' | 'PAID' | 'FAILED' | 'REFUNDED';

export interface Payment {
  bookingId: string;
  mode: PaymentMode;
  status: PaymentStatus;
  amount: string;
  transactionId?: string | null;
  cardLast4?: string | null;
  cardHolder?: string | null;
  paidAt?: string | null;
  refundedAt?: string | null;
}
